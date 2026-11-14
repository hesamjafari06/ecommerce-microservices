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

import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestClient orderServiceRestClient;

    @Transactional
    public UserResponse createUser(RegisterUserRequest request) {

        if (userRepository.existsByPublicId(request.getPublicId())) {
            throw new IllegalArgumentException("this publicId is occupied");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("this phone number is already registered");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("this email is already registered");
        }

        UserEntity newUser = userMapper.registerToEntity(request);
        userRepository.save(newUser);

        orderServiceRestClient.post()
                .uri("/api/order/cart")
                .header("User-Public-Id", newUser.getPublicId())
                .retrieve()
                .toBodilessEntity();

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
            if (userRepository.existsByPublicId(request.getPublicId())) {
                throw new IllegalArgumentException("this publicId is occupied");
            }
            user.setPublicId(request.getPublicId());
        }
        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("this email is already registered");
            }
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

    public UserEntity findUser(String phoneNumber, String email, String publicId) {

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
