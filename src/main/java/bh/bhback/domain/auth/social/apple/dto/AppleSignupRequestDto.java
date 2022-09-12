package bh.bhback.domain.auth.social.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AppleSignupRequestDto {
    @NotBlank
    private String socialId;
    @NotBlank
    private String nickName;
}