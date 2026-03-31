package com.aiinterview.storage.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse {
    private List<Violation> errors = new ArrayList<>();
    @Getter
    @Setter
    @Builder
    public static class Violation {
        private String name;
        private String code;
        private String message;
    }
}
