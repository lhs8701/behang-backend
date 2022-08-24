package bh.bhback.domain.post.dto;

import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.post.entity.Place;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.entity.Tag;
import bh.bhback.domain.user.dto.UserProfileDto;
import bh.bhback.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostResponseDto {
    private UserProfileDto userProfileDto;
    private byte[] imageFile;
    private String imageOriName;
    private Tag tag;
    private Place place;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;


    public PostResponseDto(Post post, byte[] imageFile){
        this.userProfileDto = new UserProfileDto(post.getUser());
        this.imageFile = imageFile;
        this.imageOriName = post.getImage().getFileOriName();
        this.tag = post.getTag();
        this.place = post.getPlace();
        this.createDate = post.getCreatedDate();
    }


}
