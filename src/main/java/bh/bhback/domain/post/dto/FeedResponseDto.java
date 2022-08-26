package bh.bhback.domain.post.dto;

import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FeedResponseDto {
    private Long id;
    private String imageUrl;


    public FeedResponseDto(Post post){
        this.id = post.getId();
        this.imageUrl = post.getImage().getFileUrl();
    }
}
