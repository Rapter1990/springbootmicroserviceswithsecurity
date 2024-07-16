package com.springbootmicroservices.authservice.model.auth.mapper;

import com.springbootmicroservices.authservice.model.auth.Token;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenToTokenResponseMapper extends BaseMapper<Token, TokenResponse> {

    @Override
    TokenResponse map(Token source);

    static TokenToTokenResponseMapper initialize() {
        return Mappers.getMapper(TokenToTokenResponseMapper.class);
    }

}