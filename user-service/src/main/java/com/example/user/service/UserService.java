package com.example.user.service;

import com.example.user.dto.request.LoginUserRequest;
import com.example.user.dto.request.RegisterUserRequest;
import com.example.user.dto.request.UpdateUserRequest;
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

        UserEntity user = findUser(request.getPhoneNumber(), request.getEmail(), request.getPublicId());

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return userMapper.EntityToResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UpdateUserRequest request, String publicId) {
        UserEntity user = findUser(null, null, publicId);

        if (request.getPublicId() != null) {
            user.setPublicId(request.getPublicId());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        return userMapper.EntityToResponse(user);
    }

    public UserResponse showUserInfo(String publicId) {
        UserEntity user = findUser(null, null, publicId);

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

    private UserEntity findUser(String phoneNumber, String email, String publicId) {

        if (publicId != null) {
            return userRepository.findByPublicId(publicId)
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        } else if (phoneNumber != null) {
            return userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        } else if (email != null) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        } else {
            throw new IllegalArgumentException("Login identifier is required");
        }
    }
}
