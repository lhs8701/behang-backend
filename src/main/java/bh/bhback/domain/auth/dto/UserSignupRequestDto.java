package bh.bhback.domain.auth.dto;

import bh.bhback.domain.user.entity.User;
import lombok.*;

import java.util.Collections;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestDto {
    private Long socialId;
    private String password;
    private String nickName;
    private String profileImage;
    private String provider;

    public User toEntity() {
        return User.builder()
                .socialId(socialId)
                .nickName(nickName)
                .profileImage(profileImage)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}