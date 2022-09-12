package bh.bhback.domain.report.entity;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.jpa.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name="id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
