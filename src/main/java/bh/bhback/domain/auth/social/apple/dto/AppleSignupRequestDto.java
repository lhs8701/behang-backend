package bh.bhback.domain.auth.social.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppleSignupRequestDto {
    private String id_token;
    private String code;
}
