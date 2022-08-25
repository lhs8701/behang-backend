package bh.bhback.global.common.jwt.dto;

import bh.bhback.domain.image.entity.Image;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;
}