package com.onlydive.onlydive.dto;

import lombok.Builder;

@Builder
public record UserResponse(String firstName, String lastName, String licence, boolean active){
}
