package com.example.user.dto.request;

public record RegisterUserRequest(
        String username,
        String password,
        String email
) {
}
