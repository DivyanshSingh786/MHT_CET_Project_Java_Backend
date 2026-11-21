package com.mhtcet.question_bank_service.dto;


public record AnswerKeyDTO(
        Long questionId,
        String correctOptionId,
        Integer marks,
        String explanation
) {}