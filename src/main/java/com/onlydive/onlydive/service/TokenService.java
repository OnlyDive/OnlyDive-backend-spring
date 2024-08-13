package com.onlydive.onlydive.service;

import com.onlydive.onlydive.exceptions.SpringOnlyDiveWebStatusException;
import com.onlydive.onlydive.model.RefreshToken;
import com.onlydive.onlydive.model.User;
import com.onlydive.onlydive.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public RefreshToken generateRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshToken.setUser(user);

        return refreshTokenRepository.save(refreshToken);
    }

    private void checkRefreshTokenWithUsername(String token, String username) {
        RefreshToken refreshToken = refreshTokenRepository.findOneByToken(token).orElseThrow(
                () -> new SpringOnlyDiveWebStatusException("Refresh token not found", HttpStatus.NOT_FOUND)
        );
        if (!refreshToken.getUser().getUsername().equals(username)) {
            throw new SpringOnlyDiveWebStatusException("Refresh Token and Username does not match", HttpStatus.UNAUTHORIZED);
        }
    }

    public void validateRefreshToken(String token, String username){
        checkRefreshTokenWithUsername(token, username);
    }

    public void deleteRefreshToken(String token, String username){
        checkRefreshTokenWithUsername(token, username);
        refreshTokenRepository.deleteByToken(token);
    }

    public Date getExpirationInDays(){
        return Date.from(Instant.now().plus(expirationAfterDays, ChronoUnit.DAYS));
    }
}
