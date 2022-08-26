package bh.bhback.global.common.jwt.dto;

import bh.bhback.domain.image.entity.Image;
import bh.bhback.global.common.jwt.entity.JwtExpiration;
import lombok.*;
import org.apache.el.parser.Token;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
