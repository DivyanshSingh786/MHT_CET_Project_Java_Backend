package com.mhtcet.evaluation_service.client;

import com.mhtcet.evaluation_service.dto.AttemptSnapshotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// BEFORE: @FeignClient(name = "test-engine-service", url = "${testengine.service.url}")
@FeignClient(name = "test-engine-service")
public interface TestEngineClient {
    @GetMapping("/api/attempts/{attemptId}")
    AttemptSnapshotDTO getAttempt(@PathVariable("attemptId") String attemptId);
}
