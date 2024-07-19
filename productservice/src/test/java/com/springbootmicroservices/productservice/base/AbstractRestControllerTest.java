package com.springbootmicroservices.productservice.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootmicroservices.productservice.builder.UserEntityBuilder;
import com.springbootmicroservices.productservice.client.UserServiceClient;
import com.springbootmicroservices.productservice.config.TokenConfigurationParameter;
import com.springbootmicroservices.productservice.model.auth.Token;
import com.springbootmicroservices.productservice.model.auth.enums.TokenClaims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractRestControllerTest extends AbstractTestContainerConfiguration {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Token mockAdminToken;

    protected Token mockUserToken;

    @Mock
    private TokenConfigurationParameter tokenConfiguration;

    @MockBean
    private UserServiceClient userServiceClient;


    @BeforeEach
    public void initializeAuth() {

        this.tokenConfiguration = new TokenConfigurationParameter();
        this.mockAdminToken = this.generate(new UserEntityBuilder().withValidAdminFields().build().getClaims());
        this.mockUserToken = this.generate(new UserEntityBuilder().withValidUserFields().build().getClaims());

        Mockito.doNothing().when(userServiceClient).validateToken(mockAdminToken.getAccessToken());
        Mockito.doNothing().when(userServiceClient).validateToken(mockAdminToken.getAccessToken());

        Mockito.when(userServiceClient.getAuthentication(mockAdminToken.getAccessToken()))
                .thenReturn(new UsernamePasswordAuthenticationToken("admin", null, AuthorityUtils.createAuthorityList("ADMIN")));

        Mockito.when(userServiceClient.getAuthentication(mockUserToken.getAccessToken()))
                .thenReturn(new UsernamePasswordAuthenticationToken("user", null, AuthorityUtils.createAuthorityList("USER")));

    }

    private Token generate(Map<String, Object> claims) {

        final long currentTimeMillis = System.currentTimeMillis();

        final Date tokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(new Date(currentTimeMillis), tokenConfiguration.getAccessTokenExpireMinute());

        final String accessToken = Jwts.builder()
                .header()
                .add(TokenClaims.TYP.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(tokenIssuedAt)
                .expiration(accessTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey())
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(new Date(currentTimeMillis), tokenConfiguration.getRefreshTokenExpireDay());

        final JwtBuilder refreshTokenBuilder = Jwts.builder();

        final String refreshToken = refreshTokenBuilder
                .header()
                .add(TokenClaims.TYP.getValue(), OAuth2AccessToken.TokenType.BEARER.getValue())
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(tokenIssuedAt)
                .expiration(refreshTokenExpiresAt)
                .signWith(tokenConfiguration.getPrivateKey())
                .claim(TokenClaims.USER_ID.getValue(), claims.get(TokenClaims.USER_ID.getValue()))
                .compact();

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();

    }

}
