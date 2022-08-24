package bh.bhback.domain.auth.social.kakao.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
public class KakaoProfile {
    private Long id;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    @ToString
    public static class KakaoAccount {
        private Profile profile;
        @Getter
        public static class Profile{
            private String nickname;
            private String profile_image_url;
        }
    }

    @Getter
    @ToString
    public static class Properties {
        private String nickname;
        private String profile_image;
    }
}