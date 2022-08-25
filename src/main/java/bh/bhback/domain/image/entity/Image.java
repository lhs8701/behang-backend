package bh.bhback.domain.image.entity;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.global.common.jpa.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @OneToOne(mappedBy = "image", fetch = FetchType.LAZY)
    private Post post;

    private String fileName; // 저장될 파일 이름(

    private String fileOriName; // 원래 파일의 이름

    private String fileUrl; // 저장할 경로
}
