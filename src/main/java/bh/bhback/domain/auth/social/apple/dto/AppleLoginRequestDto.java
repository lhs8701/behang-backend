package bh.bhback.domain.auth.social.apple.dto;

import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AppleLoginRequestDto {
    @NotBlank
    private String socialId;
    @NotBlank
    private String nickName;
}