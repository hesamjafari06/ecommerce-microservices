package com.example.user.helper;

import com.example.user.entity.UserEntity;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;

    public void save(UserEntity user) {
        userRepository.save(user);
    }
}
