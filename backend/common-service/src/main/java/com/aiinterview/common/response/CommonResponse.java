package com.aiinterview.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommonResponse<T>(
        T data,
        @JsonProperty("request_id")
        String requestId,
        String message,
        int code,
        @JsonProperty("server_time")
        long serverTime
) {
}
