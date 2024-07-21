package com.springbootmicroservices.productservice.utils;

import lombok.experimental.UtilityClass;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Utility class for converting PEM-encoded keys to {@link PublicKey} and {@link PrivateKey} objects.
 * Provides methods to parse and convert PEM-encoded public and private key strings into their respective Java security key representations.
 */
@UtilityClass
public class KeyConverter {

    /**
     * Converts a PEM-encoded public key string to a {@link PublicKey} object.
     *
     * @param publicPemKey the PEM-encoded public key string
     * @return the corresponding {@link PublicKey} object
     * @throws RuntimeException if an error occurs while reading or converting the key
     */
    public PublicKey convertPublicKey(final String publicPemKey) {

        final StringReader keyReader = new StringReader(publicPemKey);
        try {
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo
                    .getInstance(new PEMParser(keyReader).readObject());
            return new JcaPEMKeyConverter().getPublicKey(publicKeyInfo);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    /**
     * Converts a PEM-encoded private key string to a {@link PrivateKey} object.
     *
     * @param privatePemKey the PEM-encoded private key string
     * @return the corresponding {@link PrivateKey} object
     * @throws RuntimeException if an error occurs while reading or converting the key
     */
    public PrivateKey convertPrivateKey(final String privatePemKey) {

        StringReader keyReader = new StringReader(privatePemKey);
        try {
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo
                    .getInstance(new PEMParser(keyReader).readObject());
            return new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

}
