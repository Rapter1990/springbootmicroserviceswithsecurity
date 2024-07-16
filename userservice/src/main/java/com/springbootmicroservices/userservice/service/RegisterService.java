package com.springbootmicroservices.userservice.service;

import com.springbootmicroservices.userservice.model.user.User;
import com.springbootmicroservices.userservice.model.user.dto.request.RegisterRequest;

public interface RegisterService {

    User registerUser(final RegisterRequest registerRequest);

}
