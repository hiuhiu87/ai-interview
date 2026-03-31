package com.aiinterview.cms.infrastructure.security;

import com.aiinterview.cms.infrastructure.constant.SecurityConstant;
import com.aiinterview.common.constant.ExceptionConstants;
import com.aiinterview.common.constant.RequestMappingConstant;
import com.aiinterview.common.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException {
        String jwt = request.getHeader(SecurityConstant.JWT_HEADER);
        if (null != jwt && jwt.split("\\s+").length == 2 && jwt.startsWith(SecurityConstant.JWT_PREFIX_AUTHORIZATION)) {
            try {
                jwt = jwt.split("\\s+")[1];
                SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");
                String userType = claims.get("userType", String.class);

                List<? extends GrantedAuthority> grantedAuthorities;
                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
                        grantedAuthorities);
                Map<String, Object> attribute = new HashMap<>();
                attribute.put("userType", userType);
                attribute.put("userId", claims.get("userId", Long.class));
                attribute.put("fullName", claims.get("fullName", String.class));
                auth.setDetails(attribute);
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                returnErrorResponse(request, response, true);
            }
        } else {
            returnErrorResponse(request, response, false);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        for (String publicEndpoint : RequestMappingConstant.PUBLIC_ENDPOINTS) {
            if (publicEndpoint.contains(requestURI)) {
                return true;
            } else if (publicEndpoint.contains("*")) {
                String publicEndpointReplaced = publicEndpoint.replaceAll("\\*", "");
                if (requestURI.contains(publicEndpointReplaced)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void returnErrorResponse(
            HttpServletRequest request, HttpServletResponse response, boolean isReturningRefreshToken)
            throws IOException {
        String errorValue = "";
        if (isReturningRefreshToken) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    String cookieValue = cookie.getValue();
                    if ("token".equals(cookieName)) {
                        errorValue = cookieValue;
                    }
                }
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ExceptionConstants.INVALID_ACCESS_TOKEN)
                .value(errorValue)
                .build();
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
