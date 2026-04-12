package com.aiinterview.cms.controller;

import com.aiinterview.common.response.CommonResponse;
import com.aiinterview.common.response.CommonResponseFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleNotFound(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponseFactory.error(Map.of(), exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String, Object>>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("errors", exception.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getDefaultMessage()))
                .toList());
        return ResponseEntity.badRequest()
                .body(CommonResponseFactory.error(payload, "Validation failed", HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(CommonResponseFactory.error(Map.of(), exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}
