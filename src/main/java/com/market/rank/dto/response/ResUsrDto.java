package com.market.rank.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResUsrDto {
    private String usrId;
    private String mail;
    private String usrNm;
}
