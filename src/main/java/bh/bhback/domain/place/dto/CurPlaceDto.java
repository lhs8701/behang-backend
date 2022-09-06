package bh.bhback.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurPlaceDto {
    @NotNull
    Double curX;
    @NotNull
    Double curY;
}
