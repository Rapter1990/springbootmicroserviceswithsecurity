package com.springbootmicroservices.productservice.filter;

import com.springbootmicroservices.productservice.client.UserServiceClient;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomBearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserServiceClient userServiceClient;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
                                    @NonNull final HttpServletResponse httpServletResponse,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        log.debug("CustomBearerTokenAuthenticationFilter: Request received for URI: {}", httpServletRequest.getRequestURI());

        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (Token.isBearerToken(authorizationHeader)) {
            final String jwt = Token.getJwt(authorizationHeader);

            // Use Mono.fromCallable for async processing
            Mono.fromCallable(() -> {
                        userServiceClient.validateToken(jwt);
                        log.debug("Token validation succeeded for request: {}", httpServletRequest.getRequestURI());
                        return true;
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(valid -> {
                        try {
                            filterChain.doFilter(httpServletRequest, httpServletResponse);
                        } catch (IOException | ServletException e) {
                            throw new RuntimeException(e);
                        }
                        return Mono.empty();
                    })
                    .onErrorResume(e -> {
                        log.error("Token validation failed for request: {}", httpServletRequest.getRequestURI(), e);
                        try {
                            if (e instanceof FeignException.Unauthorized || e instanceof FeignException.Forbidden) {
                                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                            } else {
                                httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                            }
                            httpServletResponse.getWriter().write(e.getMessage());
                        } catch (IOException ex) {
                            log.error("Error writing response", ex);
                        }
                        return Mono.empty();
                    })
                    .block();
        } else {
            log.warn("Missing or invalid Authorization header for request: {}", httpServletRequest.getRequestURI());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}