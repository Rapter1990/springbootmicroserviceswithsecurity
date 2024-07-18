package com.springbootmicroservices.productservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootmicroservices.productservice.serializer.UsernamePasswordAuthenticationTokenMixin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(UsernamePasswordAuthenticationToken.class, UsernamePasswordAuthenticationTokenMixin.class);
        return mapper;
    }
}