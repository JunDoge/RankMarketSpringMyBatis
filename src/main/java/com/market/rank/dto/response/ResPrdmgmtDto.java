package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ResPrdmgmtDto {
    private List<String> imgs;
    private int prdId;
    private int bidPrice;
    private String title;
    private int sellPrice;
    private int highPrice;
    private int ieastPrice;
    private String endDtm;
    private String status;
    private int bid_cnt;
}
