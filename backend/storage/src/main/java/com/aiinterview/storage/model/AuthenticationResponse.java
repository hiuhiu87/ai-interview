package com.aiinterview.storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private HttpStatus status;
    private String message;
    private String callbackUri;
}
