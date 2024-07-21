package com.springbootmicroservices.userservice.model.user.mapper;

import com.springbootmicroservices.userservice.model.common.mapper.BaseMapper;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.dto.response.TokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * Mapper interface for converting between {@link Token} and {@link TokenResponse}.
 * This mapper handles the transformation of token data into a response format.
 */
@Mapper
public interface TokenToTokenResponseMapper extends BaseMapper<Token, TokenResponse> {

    /**
     * Maps a {@link Token} to a {@link TokenResponse}.
     * This method performs the mapping of the token object to the corresponding token response.
     * @param source the token to be mapped
     * @return a {@link TokenResponse} with mapped values
     */
    @Override
    TokenResponse map(Token source);

    /**
     * Initializes the {@link TokenToTokenResponseMapper} mapper.
     *
     * @return a new instance of {@link TokenToTokenResponseMapper}
     */
    static TokenToTokenResponseMapper initialize() {
        return Mappers.getMapper(TokenToTokenResponseMapper.class);
    }

}
