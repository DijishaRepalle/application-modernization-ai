package com.bilvantis.util;

import com.bilvantis.serviceImpl.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

import static com.bilvantis.config.ConfigConstants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Expose-Headers,Authorization, content-type, admin-id, hospital-id," + "access-control-request-headers,access-control-request-method,accept,origin,authorization");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            try {
                String authHeader = request.getHeader(AUTHORIZATION);
                String token = null;
                if (StringUtils.isNotBlank(authHeader)) {
                    if (authHeader.startsWith(BEARER)) {
                        token = authHeader.substring(BEARER.length());
                        Boolean authorizedUser = jwtTokenService.validateToken(token);
                        if (Boolean.FALSE.equals(authorizedUser)) {
                            log.error(UNAUTHORIZED_USER);
                            response.sendError(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_USER);
                            return;
                        }
                    } else {
                        log.error(TOKEN_INVALID);
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID);
                        return;
                    }
                } else {
                    log.error(TOKEN_NOT_PASSED);
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_NOT_PASSED);
                    return;
                }
            } catch (ExpiredJwtException ex) {
                log.error(TOKEN_EXPIRED);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_EXPIRED);
                return;
            } catch (Exception e) {
                log.error(TOKEN_INVALID);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), TOKEN_INVALID);
                return;
            }
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        Pattern authPattern = Pattern.compile(AUTH_ENDPOINT, Pattern.CASE_INSENSITIVE);
        return (authPattern.matcher(path).find());
    }
}
