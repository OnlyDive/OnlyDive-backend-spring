package com.onlydive.onlydive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
        String jwtToken;
        String refreshToken;
        String user;
        Date expires;
}