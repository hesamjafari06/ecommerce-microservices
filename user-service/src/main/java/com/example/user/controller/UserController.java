package com.example.user.controller;

import com.example.user.dto.request.LoginUserRequest;
import com.example.user.dto.request.RegisterUserRequest;
import com.example.user.dto.request.UpdateUserRequest;
import com.example.user.dto.response.UserResponse;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody RegisterUserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse loginUser(@RequestBody LoginUserRequest request) {
        return userService.loginUser(request);
    }

    @GetMapping("/{publicId}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserResponse showInfoUser(@PathVariable String publicId) {
        return userService.showUserInfo(publicId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(
            @RequestBody UpdateUserRequest request,
            @RequestHeader("User-Public-Id") String publicId
    ) {
        return userService.updateUser(request, publicId);
    }

}
