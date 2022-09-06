package bh.bhback.domain.auth.basic.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialSignupRequestDto {
    @NotBlank
    private String accessToken;
}
