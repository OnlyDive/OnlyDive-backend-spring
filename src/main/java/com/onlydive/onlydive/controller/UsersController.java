package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.dto.UserResponse;
import com.onlydive.onlydive.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUsers() {
        return status(HttpStatus.OK).body(usersService.getAllUsers());
    }

}
