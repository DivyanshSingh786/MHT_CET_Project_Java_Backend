package com.mhtcet.evaluation_service.dto;

public record AnswerKeyDTO(
        Long questionId,
        String correctOptionId,
        Integer marks,
        String explanation
) {}
