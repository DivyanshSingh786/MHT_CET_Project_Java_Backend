package com.mhtcet.evaluation_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scorecards")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Scorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String attemptId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String stream;

    private int totalAttempted;
    private int correctCount;
    private int incorrectCount;

    private int totalScore;
    private int maxPossibleScore; // 200 for both PCM and PCB
    private double accuracyPercentage;

    private LocalDateTime evaluatedAt;
}