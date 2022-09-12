package bh.bhback.domain.post.entity;


import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.report.entity.Report;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.jpa.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //현재 고려할 부분
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Embedded
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //@Embedded
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="content_id")
    private Place place;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Report> reportList = new ArrayList<>();

    public void changeUser(User user){ //연관관계 편의 메서드
        this.user = user;
        user.getPostList().add(this);
    }

    @Builder
    public Post(Image image, Tag tag, User user, Place place){
        this.image = image;
        this.tag = tag;
        this.user = user;
        this.place = place;
    }
}
