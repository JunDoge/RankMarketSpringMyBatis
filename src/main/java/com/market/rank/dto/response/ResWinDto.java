package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResWinDto {
    private String img;
    private int winPrice;
    private int prdId;
    private int sellPrice;
    private int highPrice;
    private String title;
    private int status;

}
