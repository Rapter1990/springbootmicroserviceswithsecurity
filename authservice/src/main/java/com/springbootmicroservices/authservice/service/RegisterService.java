package com.springbootmicroservices.authservice.service;

import com.springbootmicroservices.authservice.model.auth.User;
import com.springbootmicroservices.authservice.model.auth.dto.request.RegisterRequest;

public interface RegisterService {

    User registerUser(final RegisterRequest registerRequest);

}
