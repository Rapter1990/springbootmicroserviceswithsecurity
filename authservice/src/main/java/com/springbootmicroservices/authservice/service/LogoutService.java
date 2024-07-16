package com.springbootmicroservices.authservice.service;

import com.springbootmicroservices.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface LogoutService {

    CustomResponse<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest);

}
