package com.aiinterview.common.exceptions;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {

    private String code;

    private String value;

    @Builder.Default
    private List<Map<String, String>> errors = new ArrayList<>();

}
