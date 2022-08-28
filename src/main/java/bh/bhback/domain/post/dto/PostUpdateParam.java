package bh.bhback.domain.post.dto;

import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.post.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostUpdateParam {
    private Tag tag;
    private Place place;
}
