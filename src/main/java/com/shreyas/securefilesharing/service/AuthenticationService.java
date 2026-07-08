package com.shreyas.securefilesharing.service;

import com.shreyas.securefilesharing.dto.LoginRequest;
import com.shreyas.securefilesharing.dto.LoginResponse;
import com.shreyas.securefilesharing.entity.User;
import com.shreyas.securefilesharing.repository.UserRepository;
import com.shreyas.securefilesharing.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }
}