package com.springbootmicroservices.authservice.service;

import com.springbootmicroservices.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Service interface named {@link LogoutService} for handling user logout operations.
 * Provides methods for invalidating user tokens during logout.
 */
public interface LogoutService {

    /**
     * Logs out a user by invalidating the provided tokens.
     *
     * @param tokenInvalidateRequest the request containing the access and refresh tokens to be invalidated
     * @return a {@link CustomResponse} indicating the result of the logout operation
     */
    CustomResponse<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest);

}
