package com.example.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequest {
    private String publicId;
    private String password;
    private String phoneNumber;
    private String email;
}
