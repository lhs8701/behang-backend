package bh.bhback.domain.post.dto;

import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.post.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostUpdateParam {
    @NotNull
    private Tag tag;
    @NotNull
    private Place place;
}
