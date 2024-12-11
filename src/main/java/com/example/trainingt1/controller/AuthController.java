package com.example.trainingt1.controller;

import com.example.trainingt1.dto.LoginRequest;
import com.example.trainingt1.dto.LoginResponse;
import com.example.trainingt1.dto.SignupRequest;
import com.example.trainingt1.dto.SignupResponse;
import com.example.trainingt1.mapper.AuthMapper;
import com.example.trainingt1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    public SignupResponse register(@RequestBody SignupRequest signupRequest) {
        return new SignupResponse(userService.register(
                authMapper.convertFromSignupRequestToUser(signupRequest),
                signupRequest.getRoleEnums()
        ));
    }

    @PostMapping("/auth")
    public LoginResponse authenticate(@RequestBody LoginRequest loginRequest) {
        return userService.authenticate(authMapper.convertFromLoginRequestToUser(loginRequest));
    }



}
