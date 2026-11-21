package com.mhtcet.question_bank_service.service;

import com.mhtcet.question_bank_service.dto.AnswerKeyDTO;
import com.mhtcet.question_bank_service.dto.MockPaperResponse;
import com.mhtcet.question_bank_service.dto.StudentQuestionDTO;
import com.mhtcet.question_bank_service.model.Question;
import com.mhtcet.question_bank_service.model.Standard;
import com.mhtcet.question_bank_service.model.Subject;
import com.mhtcet.question_bank_service.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository repository;

    public Question createQuestion(Question question) {
        return repository.save(question);
    }

    public List<Question> createBulkQuestions(List<Question> questions) {
        return repository.saveAll(questions);
    }

    public MockPaperResponse generateMockPaper(String stream) {
        // Section 1: Physics (50 Qs) + Chemistry (50 Qs) -> 10 XI + 40 XII each
        List<StudentQuestionDTO> section1 = new ArrayList<>();
        section1.addAll(fetchSubjectMix(Subject.PHYSICS, 10, 40));
        section1.addAll(fetchSubjectMix(Subject.CHEMISTRY, 10, 40));

        // Section 2: Maths (50 Qs: 10 XI + 40 XII) OR Biology (100 Qs: 20 XI + 80 XII)
        List<StudentQuestionDTO> section2 = new ArrayList<>();
        if ("PCM".equalsIgnoreCase(stream)){
            section2.addAll(fetchSubjectMix(Subject.MATHEMATICS, 10, 40));
        }else {
            section2.addAll(fetchSubjectMix(Subject.BIOLOGY, 10, 40));
        }

        return new MockPaperResponse(section1, section2);
    }

    private List<StudentQuestionDTO> fetchSubjectMix(Subject subject, int xiCount, int xiiCount) {
        List<Question> questions = new ArrayList<>();
        List<Question> combined = new ArrayList<>();
        questions.addAll(repository.findRandomQuestions(subject.name(), Standard.XI.name(), xiCount));
        questions.addAll(repository.findRandomQuestions(subject.name(), Standard.XII.name(), xiiCount));
        return questions.stream().map(StudentQuestionDTO::fromEntity).toList();
    }

    public List<AnswerKeyDTO> getAnswerKeys(List<Long> questionIds) {
        return repository.findAllById(questionIds)
                .stream()
                .map(q -> new AnswerKeyDTO(q.getId(), q.getCorrectOptionId(), q.getMarks(), q.getExplanation()))
                .toList();
    }
}
