package com.market.rank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverSmsDto {
    private String ph_num;
    private String ph_code;
}
