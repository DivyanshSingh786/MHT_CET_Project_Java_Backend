package com.mhtcet.test_engine_service.dto;

import com.mhtcet.test_engine_service.model.AttemptStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SectionResponse(
        String attemptId,
        AttemptStatus status,
        LocalDateTime endsAt,
        List<StudentQuestionDTO> questions
) {}
