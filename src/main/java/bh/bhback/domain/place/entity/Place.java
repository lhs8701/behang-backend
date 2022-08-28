package bh.bhback.domain.place.entity;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.global.common.jpa.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Builder
public class Place extends BaseTimeEntity {

    @Id
    private Long contentId;

    private String name;
    private String address;
    private String phoneNumber;
    private Double mapX;
    private Double mapY;

    @Builder.Default
    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<Post> postList = new ArrayList<>();
}
