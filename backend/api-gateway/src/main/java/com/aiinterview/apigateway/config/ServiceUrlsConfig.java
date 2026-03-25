package com.aiinterview.apigateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.gateway.url")
@Getter
@Setter
public class ServiceUrlsConfig {

    private String cmsService;

    private String sessionService;

    private String workerService;

}
