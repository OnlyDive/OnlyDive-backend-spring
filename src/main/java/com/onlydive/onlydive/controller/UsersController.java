package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.dto.UserDto;
import com.onlydive.onlydive.model.LicenceEnum;
import com.onlydive.onlydive.service.LicencesService;
import com.onlydive.onlydive.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final LicencesService licencesService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getUsers() {
        return status(HttpStatus.OK).body(usersService.getAllUsers());
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long uid) {
        return status(HttpStatus.OK).body(usersService.getUserById(uid));
    }

    @GetMapping("/licences")
    public ResponseEntity<List<LicenceEnum>> getAllLicences() {
        return status(HttpStatus.OK).body(licencesService.getAllLicences());
    }

    @PatchMapping("/licence/{uid}")
    public ResponseEntity<String> updateLicence(@RequestBody LicenceEnum licence, @PathVariable Long uid) {
        licencesService.updateLicence(uid, licence);
        return status(HttpStatus.OK).body(licence + " licence applied for user with id = " + uid);
    }

}
