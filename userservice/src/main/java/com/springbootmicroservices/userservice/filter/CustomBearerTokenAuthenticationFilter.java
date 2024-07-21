package com.springbootmicroservices.userservice.filter;


import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.service.InvalidTokenService;
import com.springbootmicroservices.userservice.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final TokenService tokenService;
    private final InvalidTokenService invalidTokenService;

    /**
     * Processes the HTTP request and checks for Bearer token authentication.
     * <p>
     * This method extracts the Bearer token from the request, verifies its validity,
     * and checks whether the token has been invalidated. If the token is valid, it sets
     * the authentication in the SecurityContext.
     * </p>
     *
     * @param httpServletRequest  the HTTP request
     * @param httpServletResponse the HTTP response
     * @param filterChain         the filter chain to pass the request and response
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
                                    @NonNull final HttpServletResponse httpServletResponse,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        log.debug("API Request was secured with Security!");

        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (Token.isBearerToken(authorizationHeader)) {

            final String jwt = Token.getJwt(authorizationHeader);

            tokenService.verifyAndValidate(jwt);

            final String tokenId = tokenService.getId(jwt);

            invalidTokenService.checkForInvalidityOfToken(tokenId);

            final UsernamePasswordAuthenticationToken authentication = tokenService
                    .getAuthentication(jwt);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

}