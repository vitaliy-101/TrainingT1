package com.example.trainingt1.dto;

import com.example.trainingt1.entity.role.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String login;
    private String password;
    private List<RoleEnum> roleEnums;
}
