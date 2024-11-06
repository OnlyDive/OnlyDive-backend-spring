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
public class SpotCommentDto {

    Long id;

    @NotNull
    String name;

    @NotNull
    String description;

    @NotNull
    Long spotId;

    @NotNull
    String username;

    String creationDate;
}
