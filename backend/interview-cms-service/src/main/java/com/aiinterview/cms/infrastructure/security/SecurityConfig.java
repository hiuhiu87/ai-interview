package com.aiinterview.cms.infrastructure.security;

import com.aiinterview.common.constant.RequestMappingConstant;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    public static String[] PUBLIC_ENDPOINTS = RequestMappingConstant.PUBLIC_ENDPOINTS;

}
