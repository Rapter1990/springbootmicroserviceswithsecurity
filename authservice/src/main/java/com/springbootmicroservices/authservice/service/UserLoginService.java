package com.springbootmicroservices.authservice.service;

import com.springbootmicroservices.authservice.model.auth.Token;
import com.springbootmicroservices.authservice.model.auth.dto.request.LoginRequest;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;

public interface UserLoginService {

    CustomResponse<TokenResponse> login(final LoginRequest loginRequest);

}
