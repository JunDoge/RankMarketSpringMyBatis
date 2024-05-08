package com.market.rank.api.naver;

import lombok.Data;

@Data
public class NaverTokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
}
