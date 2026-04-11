package com.aiinterview.cms.infrastructure.security.service;

import com.aiinterview.cms.entity.User;
import com.aiinterview.cms.infrastructure.constant.CookieConstant;
import com.aiinterview.cms.infrastructure.security.exception.RedirectException;
import com.aiinterview.cms.infrastructure.security.response.TokenInfoResponse;
import com.aiinterview.cms.infrastructure.security.user.UserPrincipal;
import com.aiinterview.cms.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TokenProvider {

    @Value("${jwt.secret-key}")
    private String tokenSecret;

    private final long TOKEN_EXP = System.currentTimeMillis() + 2 * 60 * 60 * 100000;

    @Setter(onMethod_ = @Autowired)
    private UserRepository userRepository;

    @Setter(onMethod_ = @Autowired)
    private HttpServletRequest httpServletRequest;

    public String createToken(Authentication authentication) throws BadRequestException, JsonProcessingException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(
                () -> new RedirectException(CookieConstant.ACCOUNT_NOT_EXIST)
        );
        TokenInfoResponse tokenInfoResponse = getTokenResponse(user);

        String subject = new ObjectMapper().writeValueAsString(tokenInfoResponse);
        Map<String, Object> claims = getBodyClaims(tokenInfoResponse);

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(TOKEN_EXP))
                .signWith(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .compact();
    }

    public String generateToken(Map<String, Object> claims) throws JsonProcessingException {
        String subject = new ObjectMapper().writeValueAsString(claims);
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(TOKEN_EXP))
                .signWith(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .compact();
    }

    private TokenInfoResponse getTokenResponse(User user) {
        TokenInfoResponse response = new TokenInfoResponse();
        response.setUserId(user.getId().toString());
        response.setFullName(user.getFullName());
        response.setPictureUrl(user.getPicture());
        response.setEmail(user.getEmail());
        response.setRolesCode(List.of(user.getRole()));
        response.setRolesName(List.of(user.getRole()));
        response.setHost(httpServletRequest.getRemoteHost());
        return response;
    }

    private static Map<String, Object> getBodyClaims(TokenInfoResponse tokenInfoResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", tokenInfoResponse.getUserId());
        claims.put("userName", tokenInfoResponse.getUserName());
        claims.put("fullName", tokenInfoResponse.getFullName());
        claims.put("email", tokenInfoResponse.getEmail());
        claims.put("pictureUrl", tokenInfoResponse.getPictureUrl());
        List<String> rolesCode = tokenInfoResponse.getRolesCode();
        List<String> rolesName = tokenInfoResponse.getRolesName();
        claims.put("rolesCode", rolesCode.size() == 1 ? rolesCode.get(0) : rolesCode);
        claims.put("rolesName", rolesName.size() == 1 ? rolesName.get(0) : rolesName);
        claims.put("host", tokenInfoResponse.getHost());
        return claims;
    }

    public String getUserIdFromToken(String token) {
        Claims claims = getClaimsToken(token);
        return String.valueOf(claims.get("userId"));
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaimsToken(token);
        String email = claims.get("email", String.class);
        if (email != null && !email.isEmpty()) {
            return email;
        }
        return claims.get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = getClaimsToken(token);
        String role = claims.get("rolesCode", String.class);
        if (role != null && !role.isEmpty()) {
            return role;
        }
        return claims.get("rolesCode", String.class);
    }

    private Claims getClaimsToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

}
