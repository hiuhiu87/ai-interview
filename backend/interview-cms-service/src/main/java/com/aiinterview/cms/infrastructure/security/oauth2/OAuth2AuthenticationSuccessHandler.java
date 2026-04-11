package com.aiinterview.cms.infrastructure.security.oauth2;

import com.aiinterview.cms.infrastructure.constant.CookieConstant;
import com.aiinterview.cms.infrastructure.constant.OAuth2Constant;
import com.aiinterview.cms.infrastructure.security.exception.RedirectException;
import com.aiinterview.cms.infrastructure.security.response.TokenUriResponse;
import com.aiinterview.cms.infrastructure.security.service.RefreshTokenService;
import com.aiinterview.cms.infrastructure.security.service.TokenProvider;
import com.aiinterview.cms.utils.CookieUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Setter(onMethod_ = @Autowired)
    private TokenProvider tokenProvider;

    @Setter(onMethod_ = @Autowired)
    private RefreshTokenService refreshTokenService;

    @Setter(onMethod_ = @Autowired)
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Value("${frontend.url}")
    private String DEFAULT_TARGET_URL_CLIENT;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        try {
            Optional<String> redirectUri = CookieUtils.getCookie(request, OAuth2Constant.REDIRECT_URI_PARAM_COOKIE_NAME)
                    .map(Cookie::getValue);
            if (redirectUri.isEmpty()) throw new RedirectException("Redirect uri not found! Please try again later!");
            String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
            String token = tokenProvider.createToken(authentication);
            String refreshToken = refreshTokenService.generateRefreshToken(authentication);
            return buildSuccessUrl(targetUrl, TokenUriResponse.getState(token, refreshToken));
        } catch (RedirectException e) {
            e.printStackTrace(System.out);
            return buildErrorUri(request, response, e.getMessage());
        } catch (BadRequestException | JsonProcessingException | MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
//        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String buildErrorUri(HttpServletRequest request, HttpServletResponse response, String message) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, OAuth2Constant.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if (message.equalsIgnoreCase(CookieConstant.ACCOUNT_NOT_EXIST)) {
            CookieUtils.addCookie(response, CookieConstant.ACCOUNT_NOT_EXIST, CookieConstant.ACCOUNT_NOT_EXIST);
        } else if (message.equalsIgnoreCase(CookieConstant.ACCOUNT_NOT_HAVE_PERMISSION)) {
            CookieUtils.addCookie(response, CookieConstant.ACCOUNT_NOT_HAVE_PERMISSION, CookieConstant.ACCOUNT_NOT_HAVE_PERMISSION);
        }
        return redirectUri.orElse(DEFAULT_TARGET_URL_CLIENT);
    }

    private String buildSuccessUrl(String targetUrl, String token) throws MalformedURLException, URISyntaxException {
        return targetUrl + "?state=" + token;
    }

}
