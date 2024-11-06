package com.onlydive.onlydive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    String firstName;
    String lastName;
    String licence;
    boolean active;
}
