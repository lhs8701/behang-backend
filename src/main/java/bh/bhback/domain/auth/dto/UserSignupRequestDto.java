package bh.bhback.domain.auth.dto;

import bh.bhback.domain.user.entity.User;
import lombok.*;

import java.util.Collections;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestDto {
    private String email;
    private String password;
    private String name;
    private String nickName;
    private String provider;

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickName(nickName)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}