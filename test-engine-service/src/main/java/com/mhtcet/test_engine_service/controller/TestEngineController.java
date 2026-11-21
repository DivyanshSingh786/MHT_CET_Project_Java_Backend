package com.mhtcet.test_engine_service.controller;


import com.mhtcet.test_engine_service.dto.MockPaperResponse;
import com.mhtcet.test_engine_service.dto.SectionResponse;
import com.mhtcet.test_engine_service.model.TestAttempt;
import com.mhtcet.test_engine_service.service.TestEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class TestEngineController {

    private final TestEngineService service;

    // Start exam: POST /api/attempts/start?userId=divyansh&stream=PCM
    @PostMapping("/start")
    public ResponseEntity<SectionResponse> startTest(
            @RequestParam String userId,
            @RequestParam(defaultValue = "PCM") String stream) throws Exception {
        return ResponseEntity.ok(service.startTest(userId, stream));
    }

    // Save answer: PUT /api/attempts/{attemptId}/answer?questionId=1&option=A
    @PutMapping("/{attemptId}/answer")
    public ResponseEntity<String> saveAnswer(
            @PathVariable String attemptId,
            @RequestParam Long questionId,
            @RequestParam String option) {
        service.saveAnswer(attemptId, questionId, option);
        return ResponseEntity.ok("Answer saved");
    }

    // Lock Section 1 & unlock Section 2: POST /api/attempts/{attemptId}/next-section
    @PostMapping("/{attemptId}/next-section")
    public ResponseEntity<SectionResponse> moveToSection2(@PathVariable String attemptId) throws Exception {
        return ResponseEntity.ok(service.transitionToSection2(attemptId));
    }

    // Final Submit: POST /api/attempts/{attemptId}/submit
    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<TestAttempt> submitTest(@PathVariable String attemptId) {
        return ResponseEntity.ok(service.submitTest(attemptId));
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<TestAttempt> getAttempt(@PathVariable String attemptId) {
        return ResponseEntity.ok(service.getAttempt(attemptId));
    }

}
