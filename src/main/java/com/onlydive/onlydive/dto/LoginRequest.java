package com.onlydive.onlydive.dto;

import lombok.Builder;

@Builder
public record LoginRequest(String username, String password) {
}
