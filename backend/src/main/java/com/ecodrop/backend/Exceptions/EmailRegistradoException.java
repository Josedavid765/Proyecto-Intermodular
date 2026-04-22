package com.ecodrop.backend.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailRegistradoException extends RuntimeException {
    public EmailRegistradoException(String message) {
        super(message);
    }
}
