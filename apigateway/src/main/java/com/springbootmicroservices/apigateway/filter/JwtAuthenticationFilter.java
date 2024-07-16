package com.springbootmicroservices.apigateway.filter;

import com.springbootmicroservices.apigateway.client.UserServiceClient;
import com.springbootmicroservices.apigateway.model.Token;
import feign.FeignException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {


    public static class Config {
        // List of public endpoints that should not be filtered
        private List<String> publicEndpoints;

        public List<String> getPublicEndpoints() {
            return publicEndpoints;
        }

        public Config setPublicEndpoints(List<String> publicEndpoints) {
            this.publicEndpoints = publicEndpoints;
            return this;
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Skip filtering for public endpoints
            if (config != null && config.getPublicEndpoints().stream().anyMatch(path::startsWith)) {
                return chain.filter(exchange);
            }

            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (Token.isBearerToken(authorizationHeader)) {
                String jwt = Token.getJwt(authorizationHeader);

                // Inject UserServiceClient here
                ApplicationContext context = exchange.getApplicationContext();
                UserServiceClient userServiceClient = context.getBean(UserServiceClient.class);

                try {
                    // Call UserServiceClient to validate token
                    userServiceClient.validateToken(jwt);

                    // Continue with the chain if token is valid
                    return chain.filter(exchange);
                } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
                    log.error("Token validation failed: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                } catch (Exception e) {
                    log.error("Error validating token: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                }
            }

            return chain.filter(exchange);
        };
    }

}