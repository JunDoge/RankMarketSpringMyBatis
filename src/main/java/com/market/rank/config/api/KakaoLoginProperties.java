package com.market.rank.config.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("kakao.login")
public class KakaoLoginProperties {
    private String redirectUri;
    private String clientId;


}
