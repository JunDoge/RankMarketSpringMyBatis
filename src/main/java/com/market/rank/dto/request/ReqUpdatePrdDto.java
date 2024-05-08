package com.market.rank.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReqUpdatePrdDto {
    private String title;
    private String des;
    private String significant;
    private int ieastPrice;
    private String usrId;
    private int prdId;
}
