package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResBidDto {
    private String img;
    private String bidDtm;
    private int bidPrice;
    private int prdId;
    private String endDtm;
    private int sellPrice;
    private int highPrice;
    private int ieastPrice;
    private String title;
    private String status;
}
