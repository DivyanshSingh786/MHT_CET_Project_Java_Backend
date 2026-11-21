package com.mhtcet.test_engine_service.repository;

import com.mhtcet.test_engine_service.model.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, String> {
    List<TestAttempt> findByUserId(String userId);
}
