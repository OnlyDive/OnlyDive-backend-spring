package com.onlydive.onlydive.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NotNull
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpotDto {
    Long id;

    String name;

    Double longitude;

    Double latitude;

    Long commentCount;

    String creatorUsername;

    String creationDate;
}
