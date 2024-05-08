package com.market.rank.api.naver;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NaverSmsResponse {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
}
