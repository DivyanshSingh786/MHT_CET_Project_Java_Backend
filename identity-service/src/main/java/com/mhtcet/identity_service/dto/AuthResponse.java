package com.mhtcet.identity_service.dto;

public record AuthResponse(
        String token,
        String userId,
        String role
) {}
