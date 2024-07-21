package com.springbootmicroservices.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client interface named {@link UserServiceClient} for interacting with the User Service.
 * This client is used to perform various operations related to user management,
 * such as validating tokens.
 */
@FeignClient(name = "userservice", path = "/api/v1/users")
public interface UserServiceClient {

    /**
     * Validates the given token by making a POST request to the User Service.
     *
     * @param token the token to be validated
     */
    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);

}
