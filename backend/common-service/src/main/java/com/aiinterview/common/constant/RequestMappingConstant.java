package com.aiinterview.common.constant;

public class RequestMappingConstant {

    public static final String ALLOW_ALL = "/**";

    // route name
    public static final String CMS_SERVICE_ROUTE = "cms-service-route";
    public static final String SESSION_SERVICE_ROUTE = "session-service-route";
    public static final String WORKER_SERVICE_ROUTE = "worker-service-route";

    // api version prefix
    public static final String API_VERSION_PREFIX = "/api/v1";

    public static final String CMS_API_VERSION_PREFIX = "/cms/api/v1";
    public static final String SESSION_API_VERSION_PREFIX = "/session/api/v1";
    public static final String WORKER_API_VERSION_PREFIX = "/worker/api/v1";


    public static final String[] PUBLIC_ENDPOINTS = {
            API_VERSION_PREFIX + "/ping",
    };

}
