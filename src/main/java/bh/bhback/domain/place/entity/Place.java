package bh.bhback.domain.place.entity;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.global.common.jpa.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Place {

    @Id
    private Long contentId;

    private String name;
    private String address;
    private String phoneNumber;
    private Integer areaCode;
    private Double mapX;
    private Double mapY;

    @Builder.Default
    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Post> postList = new ArrayList<Post>();

}
