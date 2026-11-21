package com.mhtcet.test_engine_service.dto;

import java.util.List;

public record StudentQuestionDTO(
        Long id,
        String subject,
        String standard,
        String chapter,
        String questionText,
        String imageUrl,
        List<OptionItemDTO> options,
        Integer marks
) {}
