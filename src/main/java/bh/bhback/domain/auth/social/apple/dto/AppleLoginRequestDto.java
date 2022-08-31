package bh.bhback.domain.auth.social.apple.dto;

import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AppleLoginRequestDto {
    private String socialId;
    private String nickName;
}