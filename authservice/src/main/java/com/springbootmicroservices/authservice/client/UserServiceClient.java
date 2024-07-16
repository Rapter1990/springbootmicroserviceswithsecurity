package com.springbootmicroservices.authservice.client;

import com.springbootmicroservices.authservice.model.auth.User;
import com.springbootmicroservices.authservice.model.auth.dto.request.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userservice", path = "/api/v1/users")
public interface UserServiceClient {

    @PostMapping("/register")
    ResponseEntity<User> register(@RequestBody RegisterRequest request);

    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);

}
