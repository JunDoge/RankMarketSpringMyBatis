package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResPrdDto {
    private int prd_id;
    private List<String> imgs;
    private String title;
    private int sell_price;
    private int high_price;
    private int ieast_price;
    private int bid_price;
    private String des;
    private String end_dtm;
    private String significant;
}
