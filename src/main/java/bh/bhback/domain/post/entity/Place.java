package bh.bhback.domain.post.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Place {
    private String name;
    private String address;
    private String phoneNumber;
    private Long contentId;
    private Double mapx;
    private Double mapy;
}
