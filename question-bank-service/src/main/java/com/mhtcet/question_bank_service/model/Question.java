package com.mhtcet.question_bank_service.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Standard standard;

    @Column(nullable = false)
    private String chapter;

    @Column(nullable = false, length = 2000)
    private String questionText;

    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    private List<OptionItem> options;

    @Column(nullable = false)
    private String correctOptionId; // e.g., "A", "B", "C", "D"

    @Column(length = 2000)
    private String explanation;

    @Column(nullable = false)
    private Integer marks;

    @PrePersist
    @PreUpdate
    public void assignMhtCetMarks() {
        this.marks = (this.subject == Subject.MATHEMATICS) ? 2 : 1;
    }

}
