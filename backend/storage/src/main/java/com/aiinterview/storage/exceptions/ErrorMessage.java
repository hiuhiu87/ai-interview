package com.aiinterview.storage.exceptions;

import lombok.Getter;

@Getter
public enum ErrorMessage implements ViolationErrorMessage {
    MISSING_STORAGE_CREDENTIAL("Missing Storage Credential", "" ),
    MISSING_CREDENTIAL_STORAGE_REPOSITORY("Missing Credential Storage Repository", "" ),
    INVALID_CONNECTION_STRING("Invalid Connection String","" ),
    INVALID_AUTHENTICATION_CREDENTIAL("Invalid Authentication Credential", ""),
    DUPLICATE_AUTHENTICATION_CREDENTIAL("Duplicate Authentication Credential", ""),
    INVALID_AUTHENTICATION_REQUEST("Invalid Authentication Request", ""),
    STORAGE_NOT_IMPLEMENTED("Storage Not Implemented", ""),
    MISSING_STORAGE_CONFIGURATION("Missing Storage Configuration", ""),
    STORAGE_EXECUTION_FAIL("Storage Execution Fail", ""),
    MISSING_BUCKET_NAME("Missing Bucket Name", ""),
    FILE_NOT_FOUND("File Not Found","" ),
    INVALID_VERSION_ID("Invalid Version Id", "");

    private final String message;
    private final String code;

    ErrorMessage(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
