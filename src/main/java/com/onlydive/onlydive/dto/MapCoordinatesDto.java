package com.onlydive.onlydive.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapCoordinatesDto {
    @NotNull Double minLatitude;
    @NotNull Double maxLatitude;
    @NotNull Double minLongitude;
    @NotNull Double maxLongitude;
}
