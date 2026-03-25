package com.aiinterview.common.exceptions;

import com.aiinterview.common.constant.ErrorCode;
import com.aiinterview.common.constant.ExceptionConstants;
import com.aiinterview.common.constant.ValidationConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorResponse> handleErrorException(ErrorException ex, WebRequest webRequest) {
        exceptionLogging(ex, webRequest);
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorResponse());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest webRequest) {
        exceptionLogging(ex, webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .code(ExceptionConstants.DATA_INTEGRITY_VIOLATION)
                                .value("")
                                .build());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindingException(BindException ex, WebRequest webRequest) {
        exceptionLogging(ex, webRequest);
        List<Map<String, String>> errors = ex.getBindingResult().getAllErrors().stream()
                .map(FieldError.class::cast)
                .map(
                        value -> {
                            Map<String, String> errorMap = new HashMap<>();
                            errorMap.put(ValidationConstants.FIELD, value.getField());
                            errorMap.put(ValidationConstants.CODE, ValidationConstants.PARAM_INVALID);
                            // errorMap.put(CODE, value.getDefaultMessage());
                            return errorMap;
                        })
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().code(ValidationConstants.VALIDATION_CODE).errors(errors).build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest webRequest) {
        exceptionLogging(ex, webRequest);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder().code(ErrorCode.Auth.PERMISSION_DENIED).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest webRequest) {
        var stackTrace = ExceptionUtils.getStackTrace(ex);
        log.error("stackTrace: {}", stackTrace);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().code(ExceptionConstants.SERVER_ERROR).value("").build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .code(ExceptionConstants.MISSING_REQUIRED_PARAMETER)
                                .value("")
                                .build());
    }

    private void exceptionLogging(Exception ex, WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String httpsMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        log.error("{}, {}, Error: {}",
                httpsMethod,
                requestUri,
                ex.getMessage());
    }
}
