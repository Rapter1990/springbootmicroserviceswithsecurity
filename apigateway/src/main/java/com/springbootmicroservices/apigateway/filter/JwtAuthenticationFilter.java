package com.springbootmicroservices.apigateway.filter;

import com.springbootmicroservices.apigateway.client.UserServiceClient;
import com.springbootmicroservices.apigateway.model.Token;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

/**
 * A custom Gateway filter named {@link JwtAuthenticationFilter} that handles JWT authentication for requests.
 * This filter validates JWT tokens for all requests except those to public endpoints.
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    /**
     * Configuration class for JwtAuthenticationFilter.
     * It holds a list of public endpoints that should not be filtered.
     */
    public static class Config {
        // List of public endpoints that should not be filtered
        private List<String> publicEndpoints;

        /**
         * Gets the list of public endpoints.
         *
         * @return the list of public endpoints
         */
        public List<String> getPublicEndpoints() {
            return publicEndpoints;
        }

        /**
         * Sets the list of public endpoints.
         *
         * @param publicEndpoints the list of public endpoints to set
         * @return the updated Config object
         */
        public Config setPublicEndpoints(List<String> publicEndpoints) {
            this.publicEndpoints = publicEndpoints;
            return this;
        }

    }

    /**
     * Applies the JWT authentication filter to the gateway.
     *
     * @param config the configuration for the filter
     * @return the gateway filter
     */
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

                return Mono.fromCallable(() -> {
                            userServiceClient.validateToken(jwt);
                            log.debug("Token validation succeeded for path: {}", path);
                            return true;
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(valid -> chain.filter(exchange))
                        .onErrorResume(e -> {
                            log.error("Token validation failed for path: {}", path, e);
                            if (e instanceof FeignException.Unauthorized || e instanceof FeignException.Forbidden) {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            } else {
                                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            return exchange.getResponse().setComplete();
                        });
            }
            log.warn("Missing or invalid Authorization header for path: {}", path);
            return chain.filter(exchange);
        };
    }

}