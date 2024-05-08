package com.market.rank.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReqPayDto {
    private List<Integer> prdIds;
    private List<Integer> payPrice;
    private String payDtm;
    private String title;
}
