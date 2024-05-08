package com.market.rank.api.naver;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NaverSmsRequest {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<NaverSmsMessages> messages;




}
