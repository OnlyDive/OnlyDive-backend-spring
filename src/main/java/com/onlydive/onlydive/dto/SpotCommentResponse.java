package com.onlydive.onlydive.dto;

import lombok.Builder;

@Builder
public record SpotCommentResponse(Long id, String name, String description,Long spotId,
                                  String username,String creationDate ) {
}
