package com.example.user.service;

import com.example.user.dto.request.LoginUserRequest;
import com.example.user.dto.request.RegisterUserRequest;
import com.example.user.dto.response.UserResponse;
import com.example.user.mapper.UserMapper;
import com.example.user.model.UserEntity;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(RegisterUserRequest request) {
        UserEntity newUser = userMapper.registerToEntity(request);
        userRepository.save(newUser);
        return userMapper.EntityToResponse(newUser);
    }

    public UserResponse loginUser(LoginUserRequest request) {

        validateLoginRequest(request);

        UserEntity user = findUser(request);

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return userMapper.EntityToResponse(user);
    }

    private void validateLoginRequest(LoginUserRequest request) {

        int identifierCount = 0;

        if (request.getPublicId() != null) identifierCount++;
        if (request.getPhoneNumber() != null) identifierCount++;
        if (request.getEmail() != null) identifierCount++;

        if (identifierCount != 1) {
            throw new IllegalArgumentException(
                    "Exactly one login identifier must be provided"
            );
        }
    }

    private UserEntity findUser(LoginUserRequest request) {

        if (request.getPublicId() != null) {
            return userRepository.findByPublicId(request.getPublicId())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        } else if (request.getPhoneNumber() != null) {
            return userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        } else if (request.getEmail() != null) {
            return userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        } else {
            throw new IllegalArgumentException("Login identifier is required");
        }
    }

}
