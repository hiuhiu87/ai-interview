package com.aiinterview.cms.infrastructure.constant;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstant {

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX_AUTHORIZATION = "Bearer";

    public static String JWT_SECRET = "";

    @Value("${jwt.secret-key}")
    public String jwtSecret = null;

    @PostConstruct
    public void init() {
        SecurityConstant.JWT_SECRET = jwtSecret;
    }

}
