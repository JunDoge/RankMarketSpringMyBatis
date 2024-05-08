package com.market.rank.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReqSignUpUsrDto {
    private String mail;
    private String usr_nm;
    private String bdate;
    private String ph_num;
    private String pst_addr;
    private String rd_addr;
    private String det_addr;
    private String device_token;
}
