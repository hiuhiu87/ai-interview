package com.aiinterview.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class ErrorException extends RuntimeException {

    private HttpStatus status;

    protected ErrorResponse errorResponse;

    public ErrorException(HttpStatus status, String errorCode) {
        setStatus(status);

        ErrorResponse errorResponse = ErrorResponse.builder().code(errorCode).build();
        setErrorResponse(errorResponse);
    }

    public ErrorException(HttpStatus status, List<Map<String, String>> errors) {
        setStatus(status);

        ErrorResponse errorResponse = ErrorResponse.builder().errors(errors).build();
        setErrorResponse(errorResponse);
    }

    public ErrorException(HttpStatus status, String errorCode, List<Map<String, String>> errors) {
        setStatus(status);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .errors(errors)
                .build();
        setErrorResponse(errorResponse);
    }

    public ErrorException(HttpStatus status, String errorCode, String valueCode, List<Map<String, String>> errors) {
        setStatus(status);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .value(valueCode)
                .errors(errors)
                .build();
        setErrorResponse(errorResponse);
    }

}
