package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.RegisterRequest;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.model.NotificationEmail;
import com.onlydive.onlydive.model.User;
import com.onlydive.onlydive.model.VerificationToken;
import com.onlydive.onlydive.repository.UserRepository;
import com.onlydive.onlydive.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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

    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
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
}
