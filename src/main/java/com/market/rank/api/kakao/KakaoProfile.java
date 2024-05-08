package com.market.rank.api.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class KakaoProfile {
    @JsonProperty("id")
    private long id;

    @JsonProperty("connected_at")
    private String connected_at;
    @JsonProperty("properties")
    private Properties properties;
    @JsonProperty("kakao_account")
    private Kakao_account kakao_account;

    @Data
    @NoArgsConstructor
    public static  class Kakao_account {
        private boolean profile_nickname_needs_agreement;
        private boolean profile_image_needs_agreement;
        private Profile profile;
        private boolean has_email;
        private boolean email_needs_agreement;
        private String is_email_valid;
        private String is_email_verified;
        private String email;
        private boolean has_age_range;
        private boolean age_range_needs_agreement;
        private String age_range;
    }
    @Data
    @NoArgsConstructor
    public static  class Profile {
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private String is_default_image;

    }
    @Data
    @NoArgsConstructor
    public static  class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

}