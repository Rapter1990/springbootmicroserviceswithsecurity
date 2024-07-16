package com.springbootmicroservices.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userservice", path = "/api/v1/users")
public interface UserServiceClient {

    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);

}
