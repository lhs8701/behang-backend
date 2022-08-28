package bh.bhback.domain.place.service;

import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.place.repository.PlaceRepository;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;

    /**
     * @param contentId(장소 고유 코드)
     * @return 같은 장소에 대한 Post들
     */
    public List<PostResponseDto> getPostListByContentId(Long contentId) {
        Place place = placeRepository.findByContentId(contentId);
        List<Post> postList = place.getPostList();

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            String fileUrl = post.getImage().getFileUrl();
            File file = new File(fileUrl);

            //PostResponseDto 만들기
            PostResponseDto postResponseDto = new PostResponseDto(post);

            //List에 만든 Dto 넣기
            postResponseDtoList.add(postResponseDto);
        }

        return postResponseDtoList;
    }

    private static Double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static Double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    /**
     * @param x1 현재 위도
     * @param y1 현재 경도
     * @param x2 목적지 위도
     * @param y2 목적지 경도
     * @return 현재 위치와 목적지 사이의 거리 (m)
     */
    public static Double getDistance(Double x1, Double y1, Double x2, Double y2) {

        Double theta = y1 - y2;
        Double dist = Math.sin(deg2rad(x1)) * Math.sin(deg2rad(x2)) + Math.cos(deg2rad(x1)) * Math.cos(deg2rad(x2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        return dist; //단위 meter
    }
}
