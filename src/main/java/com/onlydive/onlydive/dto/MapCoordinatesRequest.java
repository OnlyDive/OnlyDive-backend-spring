package com.onlydive.onlydive.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MapCoordinatesRequest(@NotNull Double minLatitude,@NotNull Double maxLatitude,
                                    @NotNull Double minLongitude,@NotNull Double maxLongitude) {
}
