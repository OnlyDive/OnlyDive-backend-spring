package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.dto.LoginRequest;
import com.onlydive.onlydive.dto.AuthResponse;
import com.onlydive.onlydive.dto.RefreshTokenRequest;
import com.onlydive.onlydive.dto.SignUpRequest;
import com.onlydive.onlydive.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("/verifyAccount/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
            authService.verifyAccount(token);
            return status(HttpStatus.OK)
                    .body("Account Activated Successfully");
    }

    @PostMapping("/logIn")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return new  ResponseEntity<>(authService.login(loginRequest),HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshTokens(@Validated @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authService.refreshToken(refreshTokenRequest),HttpStatus.OK);
    }

    @PostMapping("/logOut")
    public ResponseEntity<String> logout(@Validated @RequestBody RefreshTokenRequest refreshTokenRequest) {
        authService.deleteRefreshToken(refreshTokenRequest);
        return new ResponseEntity<>("Succesfully Logout", HttpStatus.ACCEPTED);
    }
}
