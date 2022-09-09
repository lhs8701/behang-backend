package bh.bhback.domain.auth.social.kakao.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSignupRequestDto {
    @NotBlank
    private String accessToken;
}
