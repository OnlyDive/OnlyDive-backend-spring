package com.onlydive.onlydive.dto;

import lombok.Builder;

@Builder
public record RefreshTokenRequest(String refreshToken, String username) {
}

