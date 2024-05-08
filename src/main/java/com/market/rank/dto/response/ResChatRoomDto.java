package com.market.rank.dto.response;

import lombok.*;

import java.util.List;


@Getter
@Setter
public class ResChatRoomDto {
    private List<ResChatDto> chatDto;
    private String usr_id;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResChatDto {
        private String chat_id;
        private int prd_id;
        private String prd_title;

    }
}
