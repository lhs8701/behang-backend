package bh.bhback.domain.user.dto;
import bh.bhback.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserProfileDto {
    private Long userId;
    private String nickName;
    private String profileImage;

    public UserProfileDto(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.profileImage = user.getProfileImage();
    }
}
