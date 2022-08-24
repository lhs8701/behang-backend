package bh.bhback.domain.user.dto;

import bh.bhback.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
public class UserResponseDto {
    private final Long userId;
    private final String nickName;
    private final String profileImage;
    private List<String> roles;
    private Collection<? extends GrantedAuthority> authorities;
    private final LocalDateTime modifiedDate;


    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.profileImage = user.getProfileImage();
        this.roles = user.getRoles();
        this.authorities = user.getAuthorities();
        this.modifiedDate = user.getModifiedDate();
    }
}