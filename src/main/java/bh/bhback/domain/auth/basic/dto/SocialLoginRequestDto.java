package bh.bhback.domain.auth.basic.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginRequestDto {
    private String accessToken;
}