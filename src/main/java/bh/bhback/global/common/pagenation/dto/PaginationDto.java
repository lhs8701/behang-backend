package bh.bhback.global.common.pagenation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaginationDto {

    private int startIndex;
    private int endIndex;

}
