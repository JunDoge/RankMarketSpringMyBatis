package com.market.rank.config.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("aws.s3")
@Setter
@Getter
public class AwsProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
}
