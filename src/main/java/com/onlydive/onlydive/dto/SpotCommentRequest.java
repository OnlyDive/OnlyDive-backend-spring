package com.onlydive.onlydive.dto;

import lombok.Builder;

@Builder
public record SpotCommentRequest(String name, String description,Long spotId) {
}
