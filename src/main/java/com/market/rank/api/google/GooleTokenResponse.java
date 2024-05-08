package com.market.rank.api.google;

import lombok.Data;

@Data
public class GooleTokenResponse {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
