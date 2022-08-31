package bh.bhback.domain.auth.social.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleSignupResponseDto {
    private String refreshToken;
}
