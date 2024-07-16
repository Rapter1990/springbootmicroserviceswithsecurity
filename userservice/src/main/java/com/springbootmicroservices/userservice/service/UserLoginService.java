package com.springbootmicroservices.userservice.service;

import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.dto.request.LoginRequest;

public interface UserLoginService {

    Token login(final LoginRequest loginRequest);

}
