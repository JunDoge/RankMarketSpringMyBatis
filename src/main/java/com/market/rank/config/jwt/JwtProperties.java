package com.market.rank.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String secretKey;
    private String accessToken;
    private String refreshToken;
}
