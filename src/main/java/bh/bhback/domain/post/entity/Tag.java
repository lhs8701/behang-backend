package bh.bhback.domain.post.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Tag {
    private boolean convenientParking;
    private boolean comfortablePubTransit;
    private boolean withChild;
    private boolean indoor;
    private boolean withLover;
    private boolean withMyDog;
}
