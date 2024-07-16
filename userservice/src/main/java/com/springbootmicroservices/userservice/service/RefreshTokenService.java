package com.springbootmicroservices.userservice.service;

import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenRefreshRequest;

public interface RefreshTokenService {

    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);

}
