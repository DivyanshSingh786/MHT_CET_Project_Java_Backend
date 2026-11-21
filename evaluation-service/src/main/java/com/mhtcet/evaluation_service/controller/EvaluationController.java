package com.mhtcet.evaluation_service.controller;

import com.mhtcet.evaluation_service.model.Scorecard;
import com.mhtcet.evaluation_service.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final ScoringService scoringService;

    // Trigger grading: POST /api/evaluations/{attemptId}
    @PostMapping("/{attemptId}")
    public ResponseEntity<Scorecard> evaluate(@PathVariable String attemptId) {
        return ResponseEntity.ok(scoringService.evaluateAttempt(attemptId));
    }
}
