package bh.bhback.domain.user.dto;

import bh.bhback.domain.user.entity.User;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    private String email;
    private String nickName;

    @Builder
    public UserRequestDto(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickName(nickName)
                .build();
    }
}