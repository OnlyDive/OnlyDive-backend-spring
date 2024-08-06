package com.onlydive.onlydive.service;

import com.onlydive.onlydive.model.RefreshToken;
import com.onlydive.onlydive.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Scope("singleton")
@Transactional
public class TokenService {

    @Value("${custom.jwt.expiration-after-days}")
    private Long expirationAfterDays;

    private final SecretKey secretKey;
    private final RefreshTokenRepository refreshTokenRepository;


    public String generateJwtToken(Authentication authResult) {
        return Jwts.builder()
                .subject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .issuedAt(new Date())
                .expiration(getExpirationInDays())
                .signWith(secretKey)
                .compact();
    }

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepository.findOneByToken(token).orElseThrow();
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

    public Date getExpirationInDays(){
        return java.sql.Date.valueOf(LocalDate.now().plusDays(expirationAfterDays));
    }
}
