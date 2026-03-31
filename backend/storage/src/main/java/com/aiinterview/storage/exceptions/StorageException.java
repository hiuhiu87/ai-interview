package com.aiinterview.storage.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StorageException extends RuntimeException{
    private final ValidationErrorResponse violations;

    public StorageException(ValidationErrorResponse violations, Throwable e) {
        super(e);
        this.violations = violations;
    }

    public StorageException(ValidationErrorResponse violations) {
        super(violations.getErrors().stream().map(ValidationErrorResponse.Violation::getMessage).collect(Collectors.joining(", ")));
        this.violations = violations;
    }

    public StorageException(ValidationErrorResponse.Violation violation, Throwable e){
        super(e);
        this.violations = new ValidationErrorResponse();
        this.violations.setErrors(List.of(violation));
    }

    public StorageException(ValidationErrorResponse.Violation violation){
        super(violation.getMessage());
        this.violations = new ValidationErrorResponse();
        this.violations.setErrors(List.of(violation));
    }

    public StorageException(ViolationErrorMessage violation){
        super(violation.getMessage());
        this.violations = new ValidationErrorResponse();
        this.violations.setErrors(List.of(ValidationErrorResponse.Violation.builder().message(violation.getMessage()).name(violation.getCode()).build()));
    }

    public StorageException(ViolationErrorMessage violation, Throwable e){
        super(violation.getMessage(),e);
        this.violations = new ValidationErrorResponse();
        this.violations.setErrors(List.of(ValidationErrorResponse.Violation.builder().message(violation.getMessage()).name(violation.getCode()).build()));
    }

}
