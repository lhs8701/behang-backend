package bh.bhback.domain.post.dto;

import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.entity.Tag;
import bh.bhback.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Schema(description = "포스트 등록 DTO")
public class PostRequestDto {
    @NotNull
    private Tag tag;
    @NotNull
    private Place place;

    public Post toEntity(User user, Image image, Place place) {
        return Post.builder()
                .tag(this.tag)
                .place(place)
                .user(user)
                .image(image)
                .build();
    }
}
