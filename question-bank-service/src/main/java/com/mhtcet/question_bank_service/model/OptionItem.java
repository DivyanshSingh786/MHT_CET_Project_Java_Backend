package com.mhtcet.question_bank_service.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OptionItem {
    private String optionId; // "A", "B", "C", "D"
    private String text;
}
