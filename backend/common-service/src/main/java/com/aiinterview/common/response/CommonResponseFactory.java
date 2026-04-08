package com.aiinterview.common.response;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.UUID;

public final class CommonResponseFactory {

    private static final String REQUEST_ID_ATTRIBUTE = "request_id";

    private CommonResponseFactory() {
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(data, resolveRequestId(), "OK", 0, Instant.now().getEpochSecond());
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(data, resolveRequestId(), message, 0, Instant.now().getEpochSecond());
    }

    public static <T> CommonResponse<T> error(T data, String message, int code) {
        return new CommonResponse<>(data, resolveRequestId(), message, code, Instant.now().getEpochSecond());
    }

    private static String resolveRequestId() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes servletRequestAttributes)) {
            return UUID.randomUUID().toString();
        }

        HttpServletRequest request = servletRequestAttributes.getRequest();
        Object existing = request.getAttribute(REQUEST_ID_ATTRIBUTE);
        if (existing instanceof String value && !value.isBlank()) {
            return value;
        }

        String incoming = request.getHeader("X-Request-Id");
        String requestId = incoming != null && !incoming.isBlank() ? incoming : UUID.randomUUID().toString();
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
        return requestId;
    }
}
