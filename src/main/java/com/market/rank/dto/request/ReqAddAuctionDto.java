package com.market.rank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAddAuctionDto {
    private int prdId;
    private int ieastPrice;
    private String usrId;
    private int highPrice;
}
