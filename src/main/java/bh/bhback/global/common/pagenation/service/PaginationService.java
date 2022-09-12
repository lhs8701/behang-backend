package bh.bhback.global.common.pagenation.service;

import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.global.common.pagenation.dto.PaginationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationService {

    /**
     * @param pageable Request로 받아온 Pageable 인터페이스
     * @param feedList DB에서 불러온 feed 전체 목록들
     * @return pageable의 size, page를 기준으로 arrayList에서 반환해야할 index의 시작, 끝
     */
    public PaginationDto calPage(Pageable pageable, List<FeedResponseDto> feedList) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        //page가 1이 시작인 경우
        int startIndex = page * size;

        //계산한 끝 page보다 arrayList의 크기가 더 작을때 고려
        int endIndex = Math.min((page + 1) * size, feedList.size()) - 1;

        PaginationDto paginationDto = PaginationDto.builder()
                .startIndex(startIndex)
                .endIndex(endIndex)
                .build();

        return paginationDto;
    }

}

