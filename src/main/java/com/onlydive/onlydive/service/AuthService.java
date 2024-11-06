package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.LoginDto;
import com.onlydive.onlydive.dto.AuthDto;
import com.onlydive.onlydive.dto.RefreshTokenDto;
import com.onlydive.onlydive.dto.SignUpDto;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveWebStatusException;
import com.onlydive.onlydive.model.NotificationEmail;
import com.onlydive.onlydive.model.User;
import com.onlydive.onlydive.model.VerificationToken;
import com.onlydive.onlydive.repository.UserRepository;
import com.onlydive.onlydive.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
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
@Scope("singleton")
@Slf4j
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



    public void signUp(SignUpDto signUpDto) {
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setCreated(Instant.now());
        user.setActive(false);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new SpringOnlyDiveException("Email or Username is already in use");
        }

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please Activate Your Account", user.getEmail(),
                "Thank you for signing up to OnlyDive, " +
                        "please click on the below url to activate your account : \n" +
                        serverDomain + contextPath + verificationTokenUrl + token + "\n" +
                        "This url is valid for "+verificationTokenLifespanPrettyValue), "Activation");
    }

    /**generate verification token and set token, user , expiry date in it
     *
     * @param user : User from model
     * @return token : String
     */
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


    public AuthDto login(LoginDto loginDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                loginDto.getPassword());

        authentication = authenticationManager.authenticate(authentication);

        User user = (User) authentication.getPrincipal();

        if (authentication.isAuthenticated() && !user.isActive()) {
            throw new SpringOnlyDiveWebStatusException("User is not active", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return  AuthDto.builder()
                .jwtToken(tokenService.generateJwtToken(authentication))
                .refreshToken(tokenService.generateRefreshToken(user).getToken())
                .user(loginDto.getUsername())
                .expires(tokenService.getExpirationInDays().toInstant())
                .build();
    }

    public AuthDto refreshToken(RefreshTokenDto refreshRequest) {
        tokenService.validateRefreshToken(refreshRequest.getRefreshToken(), refreshRequest.getUsername());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return AuthDto.builder()
                .jwtToken(tokenService.generateJwtToken(authentication))
                .refreshToken(refreshRequest.getRefreshToken())
                .user(refreshRequest.getUsername())
                .expires(tokenService.getExpirationInDays().toInstant())
                .build();
    }

    public void deleteRefreshToken(RefreshTokenDto refreshTokenDto) {
        tokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken(), refreshTokenDto.getUsername());
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); // todo getPrincipal() zamiast getName()
        return userRepository.findByUsername(username).orElseThrow(
                () -> new SpringOnlyDiveException("User not found")
        );
    }
}
