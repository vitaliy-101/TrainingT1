package com.example.trainingt1.mapper;

import com.example.trainingt1.dto.LoginRequest;
import com.example.trainingt1.dto.SignupRequest;
import com.example.trainingt1.dto.TaskDto;
import com.example.trainingt1.dto.TaskDtoRequest;
import com.example.trainingt1.entity.Task;
import com.example.trainingt1.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    User convertFromSignupRequestToUser(SignupRequest signupRequest);

    User convertFromLoginRequestToUser(LoginRequest loginRequest);

}
