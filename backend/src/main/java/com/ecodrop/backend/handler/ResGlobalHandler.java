package com.ecodrop.backend.handler;

import com.ecodrop.backend.Exceptions.RecursoNoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResGlobalHandler {
    @ExceptionHandler(RecursoNoEncontrado.class)
    public ResponseEntity<String> exceptionEncontrado(RecursoNoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
