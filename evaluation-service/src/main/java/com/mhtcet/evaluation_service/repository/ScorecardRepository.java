package com.mhtcet.evaluation_service.repository;

import com.mhtcet.evaluation_service.model.Scorecard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScorecardRepository extends JpaRepository<Scorecard, Long> {
    Optional<Scorecard> findByAttemptId(String attemptId);
}
