package com.mhtcet.test_engine_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "test_attempts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String attemptId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String stream; // "PCM" or "PCB"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptStatus status;

    private LocalDateTime section1StartedAt;
    private LocalDateTime section1EndsAt;

    private LocalDateTime section2StartedAt;
    private LocalDateTime section2EndsAt;

    // Stores the frozen JSON of Section 1 and Section 2 questions for this attempt
    @Lob
    @Column(columnDefinition = "TEXT")
    private String section1PaperJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String section2PaperJson;

    // Maps questionId -> selectedOptionId (e.g., 101 -> "B")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "attempt_answers", joinColumns = @JoinColumn(name = "attempt_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "selected_option")
    @Builder.Default
    private Map<Long, String> answers = new HashMap<>();
}
