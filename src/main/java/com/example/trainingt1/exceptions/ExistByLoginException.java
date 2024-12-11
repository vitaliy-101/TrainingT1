package com.example.trainingt1.exceptions;

public class ExistByLoginException extends RuntimeException {
    public <T> ExistByLoginException(Class<T> clazz, String login){
        super("User with login = " + login + " already exist");
    }
}