package com.mhtcet.question_bank_service.dto;

import com.mhtcet.question_bank_service.model.*;
import java.util.List;

public record StudentQuestionDTO(
        Long id,
        Subject subject,
        Standard standard,
        String chapter,
        String questionText,
        String imageUrl,
        List<OptionItem> options,
        Integer marks
) {
    public static StudentQuestionDTO fromEntity(Question q) {
        return new StudentQuestionDTO(
                q.getId(), q.getSubject(), q.getStandard(),
                q.getChapter(), q.getQuestionText(), q.getImageUrl(),
                q.getOptions(), q.getMarks()
        );
    }
}