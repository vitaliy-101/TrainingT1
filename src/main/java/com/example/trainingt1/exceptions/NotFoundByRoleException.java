package com.example.trainingt1.exceptions;


import com.example.trainingt1.entity.role.RoleEnum;

public class NotFoundByRoleException extends RuntimeException {
    public <T> NotFoundByRoleException(Class<T> clazz, RoleEnum role){
        super("Role with name = " + role.toString() + " does not exist");
    }
}