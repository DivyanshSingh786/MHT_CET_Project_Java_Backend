package com.mhtcet.question_bank_service.controller;


import com.mhtcet.question_bank_service.dto.AnswerKeyDTO;
import com.mhtcet.question_bank_service.dto.MockPaperResponse;
import com.mhtcet.question_bank_service.dto.StudentQuestionDTO;
import com.mhtcet.question_bank_service.model.Question;
import com.mhtcet.question_bank_service.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService service;

    @PostMapping
    public ResponseEntity<Question> addQuestion(@RequestBody Question question){
        return ResponseEntity.ok(service.createQuestion(question));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Question>> addBulkQuestions(@RequestBody List<Question> questions){
        return ResponseEntity.ok(service.createBulkQuestions(questions));
    }

    // Called by Test Engine Service (e.g., GET /api/questions/mock-paper?stream=PCM)
    @GetMapping("/mock-paper")
    public ResponseEntity<MockPaperResponse> getMockPaper(@RequestParam(defaultValue = "PCM") String stream){
        return ResponseEntity.ok(service.generateMockPaper(stream));
    }

    // Called by Evaluation Service after test submission
    @PostMapping("/answer-keys")
    public ResponseEntity<List<AnswerKeyDTO>> getAnswerKeys(@RequestBody List<Long> questionIds) {
        return ResponseEntity.ok(service.getAnswerKeys(questionIds));
    }

}
