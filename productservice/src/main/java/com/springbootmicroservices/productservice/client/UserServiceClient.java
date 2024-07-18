package com.springbootmicroservices.productservice.client;

import com.springbootmicroservices.productservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userservice", path = "/api/v1/users", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);

    @GetMapping("/authenticate")
    UsernamePasswordAuthenticationToken getAuthentication(@RequestParam String token);

}
