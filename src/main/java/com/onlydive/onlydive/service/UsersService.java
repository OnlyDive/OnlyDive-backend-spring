package com.onlydive.onlydive.service;

import com.onlydive.onlydive.dto.UserDto;
import com.onlydive.onlydive.exceptions.SpringOnlyDiveException;
import com.onlydive.onlydive.mapper.UserMapper;
import com.onlydive.onlydive.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToResponse)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::mapToResponse).orElseThrow(
                () -> new SpringOnlyDiveException("User with id " + id + " not found")
        );
    }

    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::mapToResponse).orElseThrow(
                () -> new SpringOnlyDiveException("User with username " + username + " not found"));
    }
}
