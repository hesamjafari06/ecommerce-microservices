package com.example.user.mapper;

import com.example.user.dto.request.RegisterUserRequest;
import com.example.user.dto.response.UserResponse;
import com.example.user.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity registerToEntity(RegisterUserRequest request) {
        return UserEntity.builder()
                .publicId(request.getPublicId())
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public UserResponse EntityToResponse(UserEntity User) {
        return UserResponse.builder()
                .publicId(User.getPublicId())
                .username(User.getUsername())
                .phoneNumber(User.getPhoneNumber())
                .email(User.getEmail())
                .build();
    }
}
