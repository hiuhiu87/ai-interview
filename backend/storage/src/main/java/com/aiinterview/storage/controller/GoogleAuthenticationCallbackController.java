//package com.fpt.framework.storage.controller;
//
//import com.fpt.framework.storage.authenticator.OAuth2Authenticator;
//import com.fpt.framework.storage.model.AuthenticationResponse;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/auth/google")
//@ConditionalOnProperty(prefix = "storage.google.drive", name = "enabled", havingValue = "true")
//public class GoogleAuthenticationCallbackController {
//
//    private final OAuth2Authenticator googleDriveAuthentication;
//
//    public GoogleAuthenticationCallbackController(OAuth2Authenticator googleDriveAuthentication) {
//        this.googleDriveAuthentication = googleDriveAuthentication;
//    }
//
//    @GetMapping("/callback")
//    public AuthenticationResponse authenticationCallback(ServerHttpRequest request, String code) throws IOException {
//        return googleDriveAuthentication.storeCredential(request, code);
//    }
//
//    @GetMapping("/auth")
//    public AuthenticationResponse getAuthUri(ServerHttpRequest request){
//        return googleDriveAuthentication.getAuthenticateUri(request);
//    }
//}
