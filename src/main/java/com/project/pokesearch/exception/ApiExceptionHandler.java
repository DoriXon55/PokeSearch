package com.project.pokesearch.exception;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<String> handleNotFound(HttpClientErrorException.NotFound ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon not found");
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> handleApiUnavalible(ResourceAccessException ex)
    {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Pokemon API is currently unavalible");
    }
}
