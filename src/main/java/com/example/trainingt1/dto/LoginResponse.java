package com.example.trainingt1.dto;

import com.example.trainingt1.entity.role.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String jwt;
    private String login;
    private Long id;
    private List<String> roles;
}
