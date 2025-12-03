package com.example.pro.sky.hogwartsApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Class<?> clazz, Object id) {
        super(String.format("%s with id '%s' not found",
                clazz.getSimpleName(),
                id.toString()));
    }
}