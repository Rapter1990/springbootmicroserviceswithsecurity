package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.userservice.config.TokenConfigurationParameter;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.enums.TokenClaims;
import com.springbootmicroservices.userservice.model.user.enums.UserType;
import com.springbootmicroservices.userservice.service.InvalidTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
class TokenServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private InvalidTokenService invalidTokenService;

    @Mock
    private TokenConfigurationParameter tokenConfigurationParameter;

    private KeyPair keyPair;

    @BeforeEach
    void setUp() {
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        when(tokenConfigurationParameter.getPrivateKey()).thenReturn(keyPair.getPrivate());
        when(tokenConfigurationParameter.getPublicKey()).thenReturn(keyPair.getPublic());
    }

    @Test
    void givenClaims_whenGenerateToken_thenReturnValidToken() {

        // Given
        Map<String, Object> claims = Map.of("user_id", "12345");
        when(tokenConfigurationParameter.getAccessTokenExpireMinute()).thenReturn(15);
        when(tokenConfigurationParameter.getRefreshTokenExpireDay()).thenReturn(30);

        // When
        Token token = tokenService.generateToken(claims);

        // Then
        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isNotEmpty();
        assertThat(token.getRefreshToken()).isNotEmpty();

    }

    @Test
    void givenClaimsAndRefreshToken_whenGenerateToken_thenReturnValidToken() {

        // Given
        Map<String, Object> claims = Map.of("user_id", "12345");
        String refreshToken = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .signWith(keyPair.getPrivate())
                .compact();

        when(tokenConfigurationParameter.getAccessTokenExpireMinute()).thenReturn(15);

        doNothing().when(invalidTokenService).checkForInvalidityOfToken(anyString());

        // When
        Token token = tokenService.generateToken(claims, refreshToken);

        // Then
        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isNotEmpty();
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);

    }

    @Test
    void givenToken_whenGetAuthentication_thenReturnAuthentication() {

        // Given
        String token = Jwts.builder()
                .claim(TokenClaims.USER_ID.getValue(), "12345")
                .claim(TokenClaims.USER_TYPE.getValue(), "ADMIN")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .setHeaderParam(Header.TYPE, "JWT") // Add type information
                .signWith(keyPair.getPrivate())
                .compact();

        final Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token);

        final JwsHeader jwsHeader = claimsJws.getHeader();
        final Claims payload = claimsJws.getBody();

        // Handle potential null values for jwsHeader
        String tokenType = jwsHeader.getType() != null ? jwsHeader.getType() : "";
        String algorithm = jwsHeader.getAlgorithm() != null ? jwsHeader.getAlgorithm() : "";

        // Verify the created Jwt object
        final org.springframework.security.oauth2.jwt.Jwt jwt = new org.springframework.security.oauth2.jwt.Jwt(
                token,
                payload.getIssuedAt().toInstant(),
                payload.getExpiration().toInstant(),
                Map.of(
                        TokenClaims.TYP.getValue(), tokenType,
                        TokenClaims.ALGORITHM.getValue(), algorithm
                ),
                payload
        );

        final UserType userType = UserType.valueOf(payload.get(TokenClaims.USER_TYPE.getValue()).toString());

        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userType.name()));

        // When
        UsernamePasswordAuthenticationToken authentication = tokenService.getAuthentication(token);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities()).containsExactly(new SimpleGrantedAuthority("ADMIN"));
        assertThat(authentication.getPrincipal()).isEqualTo(jwt);

    }

    @Test
    void givenValidToken_whenVerifyAndValidate_thenLogTokenIsValid() {
        // Given
        String token = Jwts.builder()
                .claim("user_id", "12345")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .signWith(keyPair.getPrivate())
                .compact();

        // When & Then
        tokenService.verifyAndValidate(token);

    }

    @Test
    void givenTokens_whenVerifyAndValidate_thenValidateEachToken() {
        // Given
        Set<String> tokens = Set.of(
                Jwts.builder().claim("user_id", "12345").issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 86400000L)).signWith(keyPair.getPrivate()).compact(),
                Jwts.builder().claim("user_id", "67890").issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 86400000L)).signWith(keyPair.getPrivate()).compact()
        );

        // When
        tokenService.verifyAndValidate(tokens);

    }

    @Test
    void givenJwt_whenGetClaims_thenReturnClaims() {
        // Given
        String token = Jwts.builder()
                .claim("user_id", "12345")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .signWith(keyPair.getPrivate())
                .compact();

        // When
        Jws<Claims> claims = tokenService.getClaims(token);

        // Then
        assertThat(claims.getBody().get("user_id")).isEqualTo("12345");
    }

    @Test
    void givenJwt_whenGetPayload_thenReturnPayload() {
        // Given
        String token = Jwts.builder()
                .claim("user_id", "12345")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .signWith(keyPair.getPrivate())
                .compact();

        // When
        Claims payload = tokenService.getPayload(token);

        // Then
        assertThat(payload.get("user_id")).isEqualTo("12345");

    }

    @Test
    void givenJwt_whenGetId_thenReturnId() {

        // Given
        String tokenId = UUID.randomUUID().toString();
        String token = Jwts.builder()
                .id(tokenId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .signWith(keyPair.getPrivate())
                .compact();

        // When
        String id = tokenService.getId(token);

        // Then
        assertThat(id).isEqualTo(tokenId);

    }

    @Test
    void givenMalformedToken_whenVerifyAndValidate_thenThrowJwtException() {

        // Given
        String malformedToken = "malformed.token.string";

        // When & Then
        assertThatThrownBy(() -> tokenService.verifyAndValidate(malformedToken))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Invalid JWT token")
                .hasCauseInstanceOf(JwtException.class);

    }

    @Test
    void givenTokenWithInvalidSignature_whenVerifyAndValidate_thenThrowJwtException() {
        // Given
        String validToken = Jwts.builder()
                .claim("user_id", "12345")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .signWith(keyPair.getPrivate()) // Sign with valid private key
                .compact();

        // Tamper with the token by modifying the signature
        String tamperedToken = validToken + "tampered"; // Simple tampering for illustration

        // When & Then
        assertThatThrownBy(() -> tokenService.verifyAndValidate(tamperedToken))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Invalid JWT token")
                .hasCauseInstanceOf(JwtException.class);
    }

    @Test
    void givenToken_whenVerifyAndValidate_thenThrowResponseStatusExceptionOnUnexpectedError() {
        // Given
        String token = Jwts.builder()
                .claim("user_id", "12345")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L)) // 1 day expiration
                .signWith(keyPair.getPrivate())
                .compact();

        // Mock the parser to throw an unexpected error
        doThrow(new RuntimeException("Unexpected error"))
                .when(tokenConfigurationParameter)
                .getPublicKey();

        // When & Then
        assertThatThrownBy(() -> tokenService.verifyAndValidate(token))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Error validating token")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void givenExpiredToken_whenVerifyAndValidate_thenThrowJwtException() {

        // Given
        String expiredToken = Jwts.builder()
                .claim("user_id", "12345")
                .issuedAt(new Date(System.currentTimeMillis() - 86400000L)) // 1 day ago
                .expiration(new Date(System.currentTimeMillis() - 43200000L)) // 12 hours ago
                .signWith(keyPair.getPrivate())
                .compact();


        // When & Then
        assertThatThrownBy(() -> tokenService.verifyAndValidate(expiredToken))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Token has expired")
                .hasCauseInstanceOf(JwtException.class);

    }


}