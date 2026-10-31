package com.example.user.service;

import com.example.user.dto.request.RegisterUserRequest;
import com.example.user.entity.UserEntity;
import com.example.user.helper.UserHelper;
import com.example.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserHelper userHelper;

    public void registerUser(RegisterUserRequest request) {
        UserEntity newUser = userMapper.RegisterToEntity(request);
        userHelper.save(newUser);
    }
}
