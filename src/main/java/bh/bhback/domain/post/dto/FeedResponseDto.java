package bh.bhback.domain.post.dto;

import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FeedResponseDto {
    private Long id;
    private Long contentId;
    private Double distance;
    private String imageUrl;

    public FeedResponseDto(Post post){
        this.id = post.getId();
        this.contentId = post.getPlace().getContentId();
        this.imageUrl = post.getImage().getFileUrl();
    }
    public FeedResponseDto(Post post, Double distance){
        this.id = post.getId();
        this.contentId = post.getPlace().getContentId();
        this.imageUrl = post.getImage().getFileUrl();
        this.distance = distance;
    }
}
