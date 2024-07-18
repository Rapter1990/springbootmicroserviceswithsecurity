package com.springbootmicroservices.productservice.serializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@JsonDeserialize(using = UsernamePasswordAuthenticationTokenDeserializer.class)
public class UsernamePasswordAuthenticationTokenMixin extends UsernamePasswordAuthenticationToken {
    public UsernamePasswordAuthenticationTokenMixin(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
