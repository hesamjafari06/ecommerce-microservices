package com.example.user.mapper;

import com.example.user.dto.request.RegisterUserRequest;
import com.example.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity RegisterToEntity(RegisterUserRequest request) {
        return UserEntity.builder()
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .build();
    }
}
