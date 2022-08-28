package bh.bhback.domain.auth.social.apple.dto;

import lombok.Getter;

@Getter
public class RetAppleSignOAuth {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String id_token;
}
