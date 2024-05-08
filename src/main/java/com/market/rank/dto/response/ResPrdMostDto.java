package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResPrdMostDto {
    private List<String> img;
    private String title;
    private int ieastPrice;
    private String des;
    private int prdId;
    private int highPrice;
}
