package com.market.rank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAddDelDtm {
    private int prdId;
    private String usrId;
    private String lat;
    private String lng;
    private String deaTime;

}


