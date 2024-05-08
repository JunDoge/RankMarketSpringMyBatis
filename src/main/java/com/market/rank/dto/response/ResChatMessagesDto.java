package com.market.rank.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class ResChatMessagesDto {
    private String chat_id;
    private String usr_id;
    private String msg;
    private Date cre_dtm;
    private int prd_id;
}
