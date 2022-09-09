package bh.bhback.domain.auth.social.kakao.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginRequestDto {
    @NotBlank
    private String accessToken;
}