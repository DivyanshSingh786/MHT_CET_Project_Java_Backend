package com.mhtcet.evaluation_service.service;

import com.mhtcet.evaluation_service.client.QuestionBankClient;
import com.mhtcet.evaluation_service.client.TestEngineClient;
import com.mhtcet.evaluation_service.dto.AnswerKeyDTO;
import com.mhtcet.evaluation_service.dto.AttemptSnapshotDTO;
import com.mhtcet.evaluation_service.model.Scorecard;
import com.mhtcet.evaluation_service.repository.ScorecardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoringService {

    private final ScorecardRepository scorecardRepository;
    private final TestEngineClient testEngineClient;
    private final QuestionBankClient questionBankClient;

    public Scorecard evaluateAttempt(String attemptId) {
        // 1. Idempotency check: return existing scorecard if already evaluated
        Optional<Scorecard> existing = scorecardRepository.findByAttemptId(attemptId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // 2. Fetch attempt snapshot from Test Engine Service (8082)
        AttemptSnapshotDTO attempt = testEngineClient.getAttempt(attemptId);
        if (!"SUBMITTED".equalsIgnoreCase(attempt.status())) {
            throw new IllegalStateException("Cannot evaluate: test status is " + attempt.status());
        }

        Map<Long, String> studentAnswers = attempt.answers();
        if (studentAnswers == null || studentAnswers.isEmpty()) {
            return saveZeroScorecard(attempt);
        }

        // 3. Fetch official answer keys & marks (+1 or +2) from Question Bank Service (8081)
        List<Long> attemptedQuestionIds = new ArrayList<>(studentAnswers.keySet());
        List<AnswerKeyDTO> keys = questionBankClient.getAnswerKeys(attemptedQuestionIds);

        Map<Long, AnswerKeyDTO> keyMap = keys.stream()
                .collect(Collectors.toMap(AnswerKeyDTO::questionId, Function.identity()));

        // 4. Apply MHT-CET Scoring Rules (No negative marking; +2 Maths, +1 Phy/Chem/Bio)
        int correct = 0;
        int incorrect = 0;
        int totalScore = 0;

        for (Map.Entry<Long, String> entry : studentAnswers.entrySet()) {
            Long qId = entry.getKey();
            String chosenOption = entry.getValue();
            AnswerKeyDTO key = keyMap.get(qId);

            if (key != null && key.correctOptionId().equalsIgnoreCase(chosenOption)) {
                correct++;
                totalScore += key.marks(); // Automatically adds 2 for Maths or 1 for Phy/Chem/Bio
            } else {
                incorrect++;
                // MHT-CET has 0 negative marking
            }
        }

        int attempted = studentAnswers.size();
        double accuracy = (attempted == 0) ? 0.0 : ((double) correct / attempted) * 100.0;

        Scorecard scorecard = Scorecard.builder()
                .attemptId(attempt.attemptId())
                .userId(attempt.userId())
                .stream(attempt.stream())
                .totalAttempted(attempted)
                .correctCount(correct)
                .incorrectCount(incorrect)
                .totalScore(totalScore)
                .maxPossibleScore(200)
                .accuracyPercentage(Math.round(accuracy * 100.0) / 100.0)
                .evaluatedAt(LocalDateTime.now())
                .build();

        return scorecardRepository.save(scorecard);
    }

    private Scorecard saveZeroScorecard(AttemptSnapshotDTO attempt) {
        Scorecard zeroCard = Scorecard.builder()
                .attemptId(attempt.attemptId())
                .userId(attempt.userId())
                .stream(attempt.stream())
                .totalAttempted(0)
                .correctCount(0)
                .incorrectCount(0)
                .totalScore(0)
                .maxPossibleScore(200)
                .accuracyPercentage(0.0)
                .evaluatedAt(LocalDateTime.now())
                .build();
        return scorecardRepository.save(zeroCard);
    }
}
