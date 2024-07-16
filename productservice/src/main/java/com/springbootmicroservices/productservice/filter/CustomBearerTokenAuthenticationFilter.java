package com.springbootmicroservices.productservice.filter;

import com.springbootmicroservices.productservice.client.UserServiceClient;
import com.springbootmicroservices.productservice.model.auth.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        log.debug("API Request was secured with Security!");

        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (Token.isBearerToken(authorizationHeader)) {
            final String jwt = Token.getJwt(authorizationHeader);
            userServiceClient.validateToken(jwt);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
