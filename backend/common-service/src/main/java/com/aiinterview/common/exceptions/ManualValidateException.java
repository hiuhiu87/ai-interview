package com.aiinterview.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ManualValidateException extends ErrorException {

    private final transient BindingResult bindingResult;

    public ManualValidateException(BindingResult bindingResult) {
        super(HttpStatus.BAD_REQUEST, "validation");
        this.bindingResult = bindingResult;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        List<Map<String, String>> errors = new LinkedList<>();
        bindingResult
                .getFieldErrors()
                .forEach(
                        fieldError ->
                                errors.add(
                                        Map.of(
                                                "field",
                                                fieldError.getField(),
                                                "code",
                                                Objects.requireNonNull(fieldError.getCode()))));
        return ErrorResponse.builder().code("validation").errors(errors).build();
    }

}
