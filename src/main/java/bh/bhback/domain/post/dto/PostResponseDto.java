package bh.bhback.domain.post.dto;

import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.entity.Tag;
import bh.bhback.domain.user.dto.UserProfileDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostResponseDto {
    private Long postId;
    private UserProfileDto userProfileDto;
    private String imageUrl;
    private String imageOriName;
    private Tag tag;
    private Place place;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.userProfileDto = new UserProfileDto(post.getUser());
        this.imageUrl = post.getImage().getFileUrl();
        this.imageOriName = post.getImage().getFileOriName();
        this.tag = post.getTag();
        this.place = post.getPlace();
        this.createDate = post.getCreatedDate();
    }
}
