package com.springbootmicroservices.userservice.config;

import com.springbootmicroservices.userservice.model.user.enums.ConfigurationParameter;
import com.springbootmicroservices.userservice.utils.KeyConverter;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Configuration
public class TokenConfigurationParameter {
    private final String issuer;
    private final int accessTokenExpireMinute;
    private final int refreshTokenExpireDay;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public TokenConfigurationParameter() {

        this.issuer = ConfigurationParameter.ISSUER.getDefaultValue();

        this.accessTokenExpireMinute = Integer.parseInt(
                ConfigurationParameter.AUTH_ACCESS_TOKEN_EXPIRE_MINUTE.getDefaultValue()
        );

        this.refreshTokenExpireDay = Integer.parseInt(
                ConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue()
        );

        this.publicKey = KeyConverter.convertPublicKey(
                ConfigurationParameter.AUTH_PUBLIC_KEY.getDefaultValue()
        );

        this.privateKey = KeyConverter.convertPrivateKey(
                ConfigurationParameter.AUTH_PRIVATE_KEY.getDefaultValue()
        );

    }

}
