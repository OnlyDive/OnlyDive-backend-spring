package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.LoginRequest;
import com.onlydive.onlydive.dto.AuthResponse;
import com.onlydive.onlydive.dto.RefreshTokenRequest;
import com.onlydive.onlydive.dto.SignUpRequest;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.model.NotificationEmail;
import com.onlydive.onlydive.model.User;
import com.onlydive.onlydive.model.VerificationToken;
import com.onlydive.onlydive.repository.UserRepository;
import com.onlydive.onlydive.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    @Value("${custom.signUp.verificationToken.lifespan}")
    private int verificationTokenLifespan;
    @Value("${custom.signUp.verificationToken.lifespan-prettyValue}")
    private String verificationTokenLifespanPrettyValue;
    @Value("${custom.signUp.verificationToken.url}")
    private String verificationTokenUrl;
    @Value("${custom.server.domain}")
    private String serverDomain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;



    public void signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setActive(false);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new SpringOnlyDiveException("Email is already in use");
        }


        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please Activate Your Account", user.getEmail(),
                "Thank you for signing up to OnlyDive, " +
                        "please click on the below url to activate your account : \n" +
                        serverDomain + contextPath + verificationTokenUrl + token + "\n" +
                        "This url is valid for "+verificationTokenLifespanPrettyValue), "Activation");
    }

    protected String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusSeconds(verificationTokenLifespan));

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new SpringOnlyDiveException("Token not found")
        );

        User user = verificationToken.getUser();

        if (user.isActive()) {
            throw new SpringOnlyDiveException("The account is already active");
        }

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new SpringOnlyDiveException("Token is expired. Log in to request a new token");
        }

        user.setActive(true);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(),
                loginRequest.password());

        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return  AuthResponse.builder()
                .jwtToken(tokenService.generateJwtToken(authentication))
                .refreshToken(tokenService.generateRefreshToken().getToken())
                .user(loginRequest.username())
                .expires(tokenService.getExpirationInDays().toInstant())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshRequest) {
        tokenService.validateRefreshToken(refreshRequest.refreshToken());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return AuthResponse.builder()
                .jwtToken(tokenService.generateJwtToken(authentication))
                .refreshToken(refreshRequest.refreshToken())
                .user(refreshRequest.username())
                .expires(tokenService.getExpirationInDays().toInstant())
                .build();
    }

    public void deleteRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        tokenService.deleteRefreshToken(refreshTokenRequest.refreshToken());
    }
}
