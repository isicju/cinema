package com.vkatit.cinema.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpHeaders;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOExceptionException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(httpStatus)
                .body(createResponseStatus(httpStatus, setMessage(e, "File read error")));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(Exception e) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Content-Status", setMessage(e, "File not found"));
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .headers(headers)
                .build();
    }

    private String setMessage(Exception e, String defaultMessage) {
        return (e.getMessage() != null) ? e.getMessage() : defaultMessage;
    }

    private record Status(int code, String status, String message) {
    }

    private Status createResponseStatus(HttpStatus httpStatus, String message) {
        return new Status(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

}