package com.mhtcet.evaluation_service.dto;

import java.util.Map;

public record AttemptSnapshotDTO(
        String attemptId,
        String userId,
        String stream,
        String status,
        Map<Long, String> answers
) {}
