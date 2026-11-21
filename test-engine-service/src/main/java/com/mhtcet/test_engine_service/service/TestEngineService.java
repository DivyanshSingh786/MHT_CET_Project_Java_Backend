package com.mhtcet.test_engine_service.service;

import com.mhtcet.test_engine_service.client.QuestionBankClient;
import com.mhtcet.test_engine_service.dto.MockPaperResponse;
import com.mhtcet.test_engine_service.dto.SectionResponse;
import com.mhtcet.test_engine_service.dto.StudentQuestionDTO;
import com.mhtcet.test_engine_service.model.AttemptStatus;
import com.mhtcet.test_engine_service.model.TestAttempt;
import com.mhtcet.test_engine_service.repository.TestAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestEngineService {

    private final TestAttemptRepository repository;
    private final QuestionBankClient questionBankClient;
    private final ObjectMapper objectMapper;

    // 1. Start Test -> Calls Question Bank via Feign & unlocks ONLY Section 1
    public SectionResponse startTest(String userId, String stream) throws Exception {
        MockPaperResponse fullPaper = questionBankClient.getMockPaper(stream);

        LocalDateTime now = LocalDateTime.now();
        TestAttempt attempt = TestAttempt.builder()
                .userId(userId)
                .stream(stream.toUpperCase())
                .status(AttemptStatus.SECTION_1_ACTIVE)
                .section1StartedAt(now)
                .section1EndsAt(now.plusMinutes(90))
                .section1PaperJson(objectMapper.writeValueAsString(fullPaper.section1()))
                .section2PaperJson(objectMapper.writeValueAsString(fullPaper.section2()))
                .build();

        TestAttempt saved = repository.save(attempt);

        return new SectionResponse(
                saved.getAttemptId(),
                saved.getStatus(),
                saved.getSection1EndsAt(),
                fullPaper.section1()
        );
    }

    // 2. Auto-save answer with strict server-side timer validation
    public void saveAnswer(String attemptId, Long questionId, String selectedOption) {
        TestAttempt attempt = getAttemptOrThrow(attemptId);
        LocalDateTime now = LocalDateTime.now();

        if (attempt.getStatus() == AttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Test is already submitted.");
        }
        if (attempt.getStatus() == AttemptStatus.SECTION_1_ACTIVE && now.isAfter(attempt.getSection1EndsAt())) {
            throw new IllegalStateException("Section 1 time (90 mins) has expired. Transition to Section 2.");
        }
        if (attempt.getStatus() == AttemptStatus.SECTION_2_ACTIVE && now.isAfter(attempt.getSection2EndsAt())) {
            throw new IllegalStateException("Section 2 time (90 mins) has expired. Submit the test.");
        }

        attempt.getAnswers().put(questionId, selectedOption);
        repository.save(attempt);
    }

    // 3. Permanently lock Section 1 and unlock Section 2 (Maths or Biology)
    public SectionResponse transitionToSection2(String attemptId) throws Exception {
        TestAttempt attempt = getAttemptOrThrow(attemptId);

        if (attempt.getStatus() != AttemptStatus.SECTION_1_ACTIVE) {
            throw new IllegalStateException("Cannot transition: current status is " + attempt.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        attempt.setStatus(AttemptStatus.SECTION_2_ACTIVE);
        attempt.setSection2StartedAt(now);
        attempt.setSection2EndsAt(now.plusMinutes(90));
        repository.save(attempt);

        List<StudentQuestionDTO> sec2Questions = objectMapper.readValue(
                attempt.getSection2PaperJson(),
                new TypeReference<List<StudentQuestionDTO>>() {}
        );

        return new SectionResponse(
                attempt.getAttemptId(),
                attempt.getStatus(),
                attempt.getSection2EndsAt(),
                sec2Questions
        );
    }

    // 4. Final Test Submission
    public TestAttempt submitTest(String attemptId) {
        TestAttempt attempt = getAttemptOrThrow(attemptId);
        attempt.setStatus(AttemptStatus.SUBMITTED);
        return repository.save(attempt);
    }

    private TestAttempt getAttemptOrThrow(String attemptId) {
        return repository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attemptId: " + attemptId));
    }

    public TestAttempt getAttempt(String attemptId) {
        return getAttemptOrThrow(attemptId);
    }
}
