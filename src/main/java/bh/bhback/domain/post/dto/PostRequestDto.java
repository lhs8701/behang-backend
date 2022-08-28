package bh.bhback.domain.post.dto;

import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.entity.Tag;
import bh.bhback.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Schema(description = "포스트 등록 DTO")
public class PostRequestDto {
    private Tag tag;
    private Place place;

    public Post toEntity(User user, Image image) {
        return Post.builder()
                .tag(this.tag)
                .place(this.place)
                .user(user)
                .image(image)
                .build();
    }

    public Post toEntityWithoutPlace(User user, Image image) {
        return Post.builder()
                .tag(this.tag)
                .user(user)
                .image(image)
                .build();
    }
    public Post toEntityTest(User user, Image image, Place place) {
        return Post.builder()
                .place(place)
                .tag(this.tag)
                .user(user)
                .image(image)
                .build();
    }
}
