package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPrdsDto {
    private String img;
    private String title;
    private int prdId;
    private int sellPrice;
    private boolean wish;
    private String endDtm;
    private int highPrice;



}
