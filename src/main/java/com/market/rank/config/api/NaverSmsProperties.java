package com.market.rank.config.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("naver.sms")
public class NaverSmsProperties {
    private String accessKey;
    private String serviceId;
    private String secretKey;
    private String from;
}
