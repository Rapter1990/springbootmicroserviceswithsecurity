package com.springbootmicroservices.productservice.filter;

import com.springbootmicroservices.productservice.client.UserServiceClient;
import com.springbootmicroservices.productservice.model.auth.JwtRecord;
import com.springbootmicroservices.productservice.model.auth.Token;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom filter named {@link CustomBearerTokenAuthenticationFilter} for handling Bearer token authentication in HTTP requests.
 * This filter extracts the Bearer token from the Authorization header,
 * validates it, and sets the authentication context if the token is valid.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomBearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserServiceClient userServiceClient;

    /**
     * Processes the incoming HTTP request and performs Bearer token authentication.
     *
     * <p>Extracts the Bearer token from the Authorization header, validates the token using
     * {@link UserServiceClient#validateToken(String)}, and sets the authentication context
     * if the token is valid. If validation fails, sets the appropriate HTTP status and
     * writes an error message to the response. The filter chain proceeds regardless of
     * token validation success or failure.</p>
     *
     * @param httpServletRequest the HTTP request
     * @param httpServletResponse the HTTP response
     * @param filterChain the filter chain to proceed with
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
                                    @NonNull final HttpServletResponse httpServletResponse,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        log.debug("CustomBearerTokenAuthenticationFilter: Request received for URI: {}", httpServletRequest.getRequestURI());

        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (Token.isBearerToken(authorizationHeader)) {
            final String jwt = Token.getJwt(authorizationHeader);

            try {
                // Validate the token synchronously
                userServiceClient.validateToken(jwt);
                log.debug("Token validation succeeded for request: {}", httpServletRequest.getRequestURI());

                // Get the authentication object
                final UsernamePasswordAuthenticationToken authentication = userServiceClient.getAuthentication(jwt);

                // Set authentication to SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (FeignException e) {
                log.error("Token validation failed for request: {}", httpServletRequest.getRequestURI(), e);

                // Handle the error response
                if (e instanceof FeignException.Unauthorized || e instanceof FeignException.Forbidden) {
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                } else {
                    httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                httpServletResponse.getWriter().write(e.getMessage());
            }
        } else {
            log.warn("Missing or invalid Authorization header for request: {}", httpServletRequest.getRequestURI());
        }

        // Proceed with the filter chain in any case
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}