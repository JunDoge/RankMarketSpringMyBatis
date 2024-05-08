package com.market.rank.api.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NaverProfile {

    private String resultcode;
    private String message;
    @JsonProperty("response")
    private Response response;

    @Data
    @NoArgsConstructor
    public class Response{
        private String id;
        private String nickname;
        private String profile_image;
        private String age;
        private String gender;
        private String email;
        private String name;
        private String birthday;
        private String birthyear;

    }

}
