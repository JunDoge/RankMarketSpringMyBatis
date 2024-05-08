package com.market.rank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAddReview {
    private int prd_id;
    private String revDes;
    private int rateScr;
}
