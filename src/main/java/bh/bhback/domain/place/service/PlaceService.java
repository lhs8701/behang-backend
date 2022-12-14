
package bh.bhback.domain.place.service;

import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.place.repository.PlaceJpaRepository;
import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.global.error.advice.exception.CPlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class PlaceService {

    private final PlaceJpaRepository placeJpaRepository;

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    /**
     * @param x1 현재 위도
     * @param y1 현재 경도
     * @param x2 목적지 위도
     * @param y2 목적지 경도
     * @return 현재 위치와 목적지 사이의 거리 (m)
     */
    public static double getDistance(double x1, double y1, double x2, double y2) {

        double theta = y1 - y2;
        double dist = Math.sin(deg2rad(x1)) * Math.sin(deg2rad(x2)) + Math.cos(deg2rad(x1)) * Math.cos(deg2rad(x2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        return dist; //단위 meter
    }
}
