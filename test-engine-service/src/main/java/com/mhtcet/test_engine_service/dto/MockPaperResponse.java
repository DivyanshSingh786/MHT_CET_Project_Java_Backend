package com.mhtcet.test_engine_service.dto;

import java.util.List;

public record MockPaperResponse(
        List<StudentQuestionDTO> section1,
        List<StudentQuestionDTO> section2
) {}
