package com.springbootmicroservices.productservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Configuration
public class FeignClientConfig {

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new CustomDecoder(objectMapper);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    private static class CustomDecoder implements Decoder {

        private final ObjectMapper objectMapper;

        public CustomDecoder(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException {
            // Handle specific HTTP status codes and throw corresponding FeignExceptions
            if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
                throw new FeignException.Unauthorized("Unauthorized", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.FORBIDDEN.value()) {
                throw new FeignException.Forbidden("Forbidden", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                throw new FeignException.NotFound("Not Found", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                throw new FeignException.MethodNotAllowed("Method Not Allowed", response.request(), response.request().body(), response.headers());
            }
            if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                throw new FeignException.BadRequest("Bad Request", response.request(), response.request().body(), response.headers());
            }

            // Deserialize the response body using Jackson
            if (response.body() != null) {
                InputStream inputStream = response.body().asInputStream();
                return objectMapper.readValue(inputStream, objectMapper.constructType(type));
            }

            return null;
        }
    }

    private static class CustomErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            HttpStatus status = HttpStatus.valueOf(response.status());
            // Handle specific HTTP status codes and return corresponding FeignExceptions
            if (status == HttpStatus.UNAUTHORIZED) {
                return new FeignException.Unauthorized("Unauthorized", response.request(), response.request().body(), response.headers());
            }
            if (status == HttpStatus.FORBIDDEN) {
                return new FeignException.Forbidden("Forbidden", response.request(), response.request().body(), response.headers());
            }
            if (status == HttpStatus.NOT_FOUND) {
                return new FeignException.NotFound("Not Found", response.request(), response.request().body(), response.headers());
            }
            if (status == HttpStatus.METHOD_NOT_ALLOWED) {
                return new FeignException.MethodNotAllowed("Method Not Allowed", response.request(), response.request().body(), response.headers());
            }
                return new FeignException.BadRequest("Bad Request", response.request(), response.request().body(), response.headers());

        }
    }
}