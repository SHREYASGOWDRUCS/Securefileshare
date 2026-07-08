package com.shreyas.securefilesharing.controller;

import com.shreyas.securefilesharing.dto.LoginRequest;
import com.shreyas.securefilesharing.dto.LoginResponse;
import com.shreyas.securefilesharing.dto.RegisterRequest;
import com.shreyas.securefilesharing.entity.User;
import com.shreyas.securefilesharing.service.AuthenticationService;
import com.shreyas.securefilesharing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }
}