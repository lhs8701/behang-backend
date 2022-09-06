package bh.bhback.domain.auth.basic.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginRequestDto {
    @NotBlank
    private String accessToken;
}