package com.aiinterview.apigateway.config;

import com.aiinterview.common.constant.RequestMappingConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class GateWayConfig {

    private final ServiceUrlsConfig serviceUrlsConfig;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // cms-config
                .route(RequestMappingConstant.CMS_SERVICE_ROUTE, r -> r.path(RequestMappingConstant.CMS_API_VERSION_PREFIX + RequestMappingConstant.ALLOW_ALL)
                        .filters(f -> f.stripPrefix(1))
                        .uri(serviceUrlsConfig.getCmsService()))

                // session-config
                .route(RequestMappingConstant.SESSION_SERVICE_ROUTE, r -> r.path(RequestMappingConstant.SESSION_API_VERSION_PREFIX + RequestMappingConstant.ALLOW_ALL)
                        .filters(f -> f.stripPrefix(1))
                        .uri(serviceUrlsConfig.getSessionService()))

                // worker-config
                .route(RequestMappingConstant.WORKER_SERVICE_ROUTE, r -> r.path(RequestMappingConstant.WORKER_API_VERSION_PREFIX + RequestMappingConstant.ALLOW_ALL)
                        .filters(f -> f.stripPrefix(1))
                        .uri(serviceUrlsConfig.getWorkerService()))

                .build();
    }

}
