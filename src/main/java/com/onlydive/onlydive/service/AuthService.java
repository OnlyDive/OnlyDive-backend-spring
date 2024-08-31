package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.*;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Scope("singleton")
@Slf4j
public class AuthService { //todo add forgot password mechanism

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

    private boolean usernameAndEmailInExpiredAccounts(User user) {
        List<User> existingUsers = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

        for (User existingUser : existingUsers) {
            if (existingUser.isActive()) return false;
        }

        List<VerificationToken> verificationTokens = verificationTokenRepository.findByUserIn(existingUsers);

        for (VerificationToken verificationToken : verificationTokens) {
            if (verificationToken.getExpiryDate().isAfter(Instant.now())) return false;
        }

        verificationTokenRepository.deleteAll(verificationTokens);
        userRepository.deleteAll(existingUsers);

        //todo send email(s) that the account has been deleted?

        return true;
    }

    public void signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setActive(false);

        if (!usernameAndEmailInExpiredAccounts(user)) {
            throw new SpringOnlyDiveException("Email or Username is already in use");
        }

        userRepository.save(user);

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
            throw new SpringOnlyDiveException("Token is expired. Sign up again");
        }

        //todo send email that the account has been verified?

        user.setActive(true);
        userRepository.save(user);
    }


    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.username(),
                loginRequest.password());

        authentication = authenticationManager.authenticate(authentication);

        User user = (User) authentication.getPrincipal();

        if (authentication.isAuthenticated() && !user.isActive()) {
            throw new SpringOnlyDiveWebStatusException("User is not active", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return  AuthResponse.builder()
                .jwtToken(tokenService.generateJwtToken(authentication))
                .refreshToken(tokenService.generateRefreshToken(user).getToken())
                .user(loginRequest.username())
                .expires(tokenService.getExpirationInDays())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshRequest) {
        tokenService.validateRefreshToken(refreshRequest.refreshToken(), refreshRequest.username());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return AuthResponse.builder()
                .jwtToken(tokenService.generateJwtToken(authentication))
                .refreshToken(refreshRequest.refreshToken())
                .user(refreshRequest.username())
                .expires(tokenService.getExpirationInDays())
                .build();
    }

    public void deleteRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        tokenService.deleteRefreshToken(refreshTokenRequest.refreshToken(), refreshTokenRequest.username());
    }

    public User getCurrentUser(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username).orElseThrow(
                () -> new SpringOnlyDiveException("User not found")
        );
    }

    public PermissionResponse getCurrentUserPermissions() {
        return new PermissionResponse(getCurrentUser().getUsername());
    }
}
