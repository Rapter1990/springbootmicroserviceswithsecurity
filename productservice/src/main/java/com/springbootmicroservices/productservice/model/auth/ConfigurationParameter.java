package com.springbootmicroservices.productservice.model.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing configuration parameters used for token management and key settings.
 * This enum provides default values for various configuration parameters related
 * to authentication, including token expiration settings and cryptographic keys.
 */
@Getter
@RequiredArgsConstructor
public enum ConfigurationParameter {

    AUTH_ACCESS_TOKEN_EXPIRE_MINUTE("30"),
    AUTH_REFRESH_TOKEN_EXPIRE_DAY("1"),
    AUTH_PUBLIC_KEY("""
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1HmZ3A379M6Rv9UnMt9R
            Wq0a6bpcnoOWJxTi2exwnecW3r1X1PjeUvsDogy7RYjhlxU0G+1r38gPWfUW2FNd
            tsa3H+FDhJ6dcNc4uKVYPsiVJukHi4NrvWA8E8dPdLW1lNcijr4PqXjvZTLoS1QX
            f30wnNLBNDwdPXTESodi/n87VoSH2ChgLZUVfoS3m/NlUN8Z58gGxRcpUyjl+MmC
            hD2cfyWr2xdxKd+UQMrd36LfyoWh0IlONlxo0H5x8JIwlziLbPEAh7dJ9QYM0b5G
            msXBAzrILvW5+POSq4u1vNlzSwdLe+AZ6bnCwQVrqvMn/I7JT+4+lY8BsP/gMRQL
            awIDAQAB
            -----END PUBLIC KEY-----
                        """),
    AUTH_PRIVATE_KEY("""
            -----BEGIN PRIVATE KEY-----
            MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDUeZncDfv0zpG/
            1Scy31FarRrpulyeg5YnFOLZ7HCd5xbevVfU+N5S+wOiDLtFiOGXFTQb7WvfyA9Z
            9RbYU122xrcf4UOEnp1w1zi4pVg+yJUm6QeLg2u9YDwTx090tbWU1yKOvg+peO9l
            MuhLVBd/fTCc0sE0PB09dMRKh2L+fztWhIfYKGAtlRV+hLeb82VQ3xnnyAbFFylT
            KOX4yYKEPZx/JavbF3Ep35RAyt3fot/KhaHQiU42XGjQfnHwkjCXOIts8QCHt0n1
            BgzRvkaaxcEDOsgu9bn485Kri7W82XNLB0t74BnpucLBBWuq8yf8jslP7j6VjwGw
            /+AxFAtrAgMBAAECggEARWpw5N7AuQsfvN+DjfA9oPU6/K9BARyWWrBNKMtBQ6Uy
            6JRNdKvV3qBZYIDuUdpVcUmhG5qmipbOxSH4U7ZwwH0NaOHscBBt+WanBlQmj2Ry
            riKlr2PBOD6Pghq0j7mp2DWs+ZuIfGKhO5u1Hp8bijA5SJLmQg19tA1I79xpcCFC
            flY8WaBKJCTKtH3sisS9IqAtGx3+O2fwzk5oSA6DDuX+45zTLS1vlucwhTUTDE4j
            UitYM5CnW9LzJ72ofWZBE29twECXQa+7YYXYniqUOzYZVhy7OiwYT9HH2fDGurmL
            F4adtcmVS6CUM6OYSV8z1DXbwLcidTqwiAj9uZ8suQKBgQD2hb/Bimgoh0dT6q9N
            pORikLW9L/6q3TuAfNJrrP63dwgooeCcHdk8Wh1zFkNibmfdhHQM5Hu3pQq0oxtG
            XiH1WYhluDAjyq6ahvYasgGtdzh279VcicleP9x6B6tmWop+a9kM+Yh2r3Yb5KXr
            ZyFWLeoEkCyeXdVr1tds119Q/QKBgQDcpMKXIAiayZmcHU03pNrDGhs7m2B18S6Z
            WcXSQRf1d98wbzzfsrbHM9k8gX5VGvZb+rwl95SyevT1LSaGM44On4WXjCUlpUnb
            8+mKONra8yXuWutB1aclPQddj4HAFGikJyBP45e/SXNTBPrK5cm/HULbgd76FxHv
            QQZ2EbSOhwKBgA5pHR98LsCHv+So6Fx6khss6GLJxnJIgmztXwOKVk11ONXfOJkH
            qaY8glIy7/d2Cr5JOttyE8VVcX3Dtxly8Ts9Y5rGnJHLDE/eKc6/rxdry7IwLOG+
            8DWBOCst/Zf7HPNs7IA0qgR+F0JkKErNeYZnIrHnl6QeShaGtYsYP+slAoGBANRU
            yd5dOWqb73NIz3Jo9w0iJmrqT52wh8OTnMeFVOUogmQ96Drt5O82eiu8AjMsS0Cg
            vkdbRoGryefXl2c2XdK8uPbqKyVbNwSwaWJW7GYf77S9UgB89ujjHh9vZtHN0hWG
            gZXf07yFlrGh7Scsk0WThy9uf4H0iZHQ5cLhrvwpAoGAAj3uW/aAJDhfHHVgBGhC
            la8OzEqY8UbqMYrXKfrmYOzvFYKWd5BgrZvgINhSmjxMeil952gHVsH/td+KUyqe
            dxTAgV9WVn8KVz6KoEKJV5CarN0IOl5fZVJz1i0Q5t6VdUXLC6teh4ByrEAlsRgi
            h2UWcQCA6KqrmJbR2q6yj+s=
            -----END PRIVATE KEY-----
            """);

    private final String defaultValue;

}
