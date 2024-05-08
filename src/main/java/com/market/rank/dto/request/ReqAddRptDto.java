package com.market.rank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAddRptDto {
    private String rptId;
    private int prdId;
    private String rptDes;
}