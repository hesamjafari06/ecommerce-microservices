package com.example.user.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    private Long id;
    private String publicId;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
}
