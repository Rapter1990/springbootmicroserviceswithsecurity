package com.springbootmicroservices.userservice.builder;

import com.springbootmicroservices.userservice.model.user.enums.TokenClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenBuilder {

    public static Claims getValidClaims(String userId, String firstName) {
        Map<String, Object> mockClaimsMap = new HashMap<>();
        mockClaimsMap.put(TokenClaims.JWT_ID.getValue(), UUID.randomUUID().toString());
        mockClaimsMap.put(TokenClaims.USER_ID.getValue(), userId);
        mockClaimsMap.put(TokenClaims.USER_FIRST_NAME.getValue(), firstName);
        return Jwts.claims(mockClaimsMap);
    }

}
