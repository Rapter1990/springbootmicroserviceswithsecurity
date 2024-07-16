package com.springbootmicroservices.userservice.service;

import com.springbootmicroservices.userservice.model.user.dto.request.TokenInvalidateRequest;

public interface LogoutService {

    void logout(final TokenInvalidateRequest tokenInvalidateRequest);

}
