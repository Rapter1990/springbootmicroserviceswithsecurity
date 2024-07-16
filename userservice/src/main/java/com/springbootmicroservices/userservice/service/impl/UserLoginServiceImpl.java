package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.exception.PasswordNotValidException;
import com.springbootmicroservices.userservice.exception.UserNotFoundException;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.dto.request.LoginRequest;
import com.springbootmicroservices.userservice.model.user.entity.UserEntity;
import com.springbootmicroservices.userservice.repository.UserRepository;
import com.springbootmicroservices.userservice.service.TokenService;
import com.springbootmicroservices.userservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @Override
    public Token login(LoginRequest loginRequest) {

        final UserEntity userEntityFromDB = userRepository
                .findUserEntityByEmail(loginRequest.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException("Can't find with given email: "
                                + loginRequest.getEmail())
                );

        if (Boolean.FALSE.equals(passwordEncoder.matches(
                loginRequest.getPassword(), userEntityFromDB.getPassword()))) {
            throw new PasswordNotValidException();
        }

        return tokenService.generateToken(userEntityFromDB.getClaims());

    }

}
