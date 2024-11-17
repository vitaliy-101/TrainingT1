package com.example.trainingt1.exceptions;

public class NotFoundByIdException extends Exception {
    public NotFoundByIdException(String entityName, Long id) {
        super(String.format("Entity named %s is not found by id = %d", entityName, id));
    }
}
