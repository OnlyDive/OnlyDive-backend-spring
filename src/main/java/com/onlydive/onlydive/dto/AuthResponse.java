package com.onlydive.onlydive.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuthResponse(String jwtToken, String refreshToken, String user, Instant expires) {
}