package com.mhtcet.identity_service.dto;

public record AuthRequest(
        String email,
        String password,
        String role
) {}
