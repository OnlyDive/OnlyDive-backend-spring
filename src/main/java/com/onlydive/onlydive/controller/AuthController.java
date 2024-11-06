package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.dto.*;
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
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        authService.signUp(signUpDto);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("/verifyAccount/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
            authService.verifyAccount(token);
            return status(HttpStatus.OK)
                    .body("Account Activated Successfully");
    }

    @PostMapping("/logIn")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto loginDto) {
        return new  ResponseEntity<>(authService.login(loginDto),HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthDto> refreshTokens(@Validated @RequestBody RefreshTokenDto refreshTokenDto) {
        return new ResponseEntity<>(authService.refreshToken(refreshTokenDto),HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Validated @RequestBody RefreshTokenDto refreshTokenDto) {
        authService.deleteRefreshToken(refreshTokenDto);
        return new ResponseEntity<>("Succesfully Logout", HttpStatus.ACCEPTED);
    }

    @PostMapping("/getCurrentUserPermissions")
    public ResponseEntity<Boolean> isUserPermitted(@Validated @RequestBody PermissionDto permissionDto) {
        return new ResponseEntity<>(authService.isUserPermitted(permissionDto),HttpStatus.OK);
    }
}
