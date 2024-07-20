package com.springbootmicroservices.userservice.utils;

import org.bouncycastle.openssl.PEMException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class KeyConverterTest {

    @Test
    void utilityClass_ShouldNotBeInstantiated() {
        assertThrows(InvocationTargetException.class, () -> {
            // Attempt to use reflection to create an instance of the utility class
            java.lang.reflect.Constructor<KeyConverter> constructor = KeyConverter.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }


    @Test
    void givenEmptyPublicKey_whenConvertPublicKey_thenThrowRuntimeException() {
        // Given
        String emptyPublicPemKey = "";

        // When & Then
        assertThatThrownBy(() -> KeyConverter.convertPublicKey(emptyPublicPemKey))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(PEMException.class)
                .hasMessageContaining("PEMException");
    }

    @Test
    void givenMalformedPrivateKey_whenConvertPrivateKey_thenThrowRuntimeException() {
        // Given
        String malformedPrivatePemKey = "-----BEGIN PRIVATE KEY-----\n" +
                "malformedkey\n" +
                "-----END PRIVATE KEY-----";

        // When & Then
        assertThatThrownBy(() -> KeyConverter.convertPrivateKey(malformedPrivatePemKey))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(PEMException.class)
                .hasMessageContaining("PEMException");
    }

    @Test
    void givenEmptyPrivateKey_whenConvertPrivateKey_thenThrowRuntimeException() {
        // Given
        String emptyPrivatePemKey = "";

        // When & Then
        assertThatThrownBy(() -> KeyConverter.convertPrivateKey(emptyPrivatePemKey))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(PEMException.class)
                .hasMessageContaining("PEMException");
    }

}