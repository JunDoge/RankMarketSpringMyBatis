package com.market.rank.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReqLoginUsrDto {
    private String usrId;
    private String mail;
    private String usrNm;
    private String token;
}
