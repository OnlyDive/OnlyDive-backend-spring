package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.dto.RegisterRequest;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        authService.signUp(registerRequest);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("/verifyAccount/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
            authService.verifyAccount(token);
            return status(HttpStatus.OK)
                    .body("Account Activated Successfully");
    }
}
