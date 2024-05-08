package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResPayDto {
    private String img;
    private String title;
    private int winPrice;
    private String payDtm;
    private boolean payDiff;
    private int prdId;
}
