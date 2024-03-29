package bh.bhback.domain.post.service;

import bh.bhback.domain.image.dto.ImageDto;
import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.image.repository.ImageJpaRepository;
import bh.bhback.domain.image.service.ImageService;
import bh.bhback.domain.place.dto.CurPlaceDto;
import bh.bhback.domain.place.entity.Place;
import bh.bhback.domain.place.repository.PlaceJpaRepository;
import bh.bhback.domain.place.service.PlaceService;
import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.dto.PostUpdateParam;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.repository.PostJpaRepository;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.common.pagenation.dto.PaginationDto;
import bh.bhback.global.common.pagenation.service.PaginationService;
import bh.bhback.global.error.advice.exception.CAccessDeniedException;
import bh.bhback.global.error.advice.exception.CPlaceNotFoundException;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import bh.bhback.global.error.advice.exception.CPostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final ImageService imageService;
    private final PlaceService placeService;
    private final PaginationService paginationService;

    @Transactional
    public Long create(PostRequestDto postRequestDto, MultipartFile file, User user) {
        Long contentId = postRequestDto.getPlace().getContentId();
        Optional<Place> placeOptional = placeJpaRepository.findByContentId(contentId);
        Place place;

        if (placeOptional.isEmpty())
            place = placeJpaRepository.save(postRequestDto.getPlace());
        else
            place = placeOptional.get();

        ImageDto imageDto = imageService.uploadPostImage(file);
        Post post = postRequestDto.toEntity(user, imageDto.toEntity(), place);
        return postJpaRepository.save(post).getId();
    }

    @Transactional
    public Long update(Long postId, PostUpdateParam postUpdateParam, User user) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(CPostNotFoundException::new);
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new CAccessDeniedException();
        }
        Long contentId = postUpdateParam.getPlace().getContentId();
        Optional<Place> placeOptional = placeJpaRepository.findByContentId(contentId);
        Place place;

        if (placeOptional.isEmpty())
            place = placeJpaRepository.save(postUpdateParam.getPlace());
        else
            place = placeOptional.get();

        post.setTag(postUpdateParam.getTag());
        post.setPlace(place);
        return postId;
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(CPostNotFoundException::new);
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new CAccessDeniedException();
        }
        Image image = post.getImage();
        postJpaRepository.deleteById(postId);
        imageService.deleteImage(image.getFileUrl());
    }

    @Transactional
    public void deleteAllPost(User user) {
        List<Post> postList = postJpaRepository.findAllByUser(user).orElse(null);
        if (postList == null)
            return;

        for (Post post : postList) {
            deletePost(post.getId(), user);
        }
    }

    @Transactional
    public PostResponseDto getPost(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(CPostNotFoundException::new);
        return new PostResponseDto(post);
    }

    @Transactional // 최신순 정렬(임시)
    public List<FeedResponseDto> getFeed(Pageable pageable) {
        List<Post> postList = postJpaRepository.findAllByOrderByCreatedDateDesc(pageable).orElse(null);
        if (postList == null)
            return null;
        return postList.stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * @param contentId(장소 고유 코드)
     * @return 같은 장소에 대한 Post들
     */
    public List<FeedResponseDto> getFeedInSamePlace(Long contentId) {
        Place place = placeJpaRepository.findByContentId(contentId).orElseThrow(CPlaceNotFoundException::new);

        return place.getPostList().stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * @param pageable
     * @param curPlaceDto (현재 위치)
     * @return 현재 부터 떨어진 거리순으로 정렬된 Feed
     */
    @Transactional
    public List<FeedResponseDto> getFeedOrderByDistance(Pageable pageable, CurPlaceDto curPlaceDto) {
        List<Post> postList = postJpaRepository.findAll();

        List<FeedResponseDto> tempFeedList = new ArrayList<>();

        double curX = curPlaceDto.getCurX();
        double curY = curPlaceDto.getCurY();

        for (Post post : postList) {
            //상대 거리 구하기
            double MapX = post.getPlace().getMapX();
            double MapY = post.getPlace().getMapY();
            double distance = placeService.getDistance(curX, curY, MapX, MapY);
            tempFeedList.add(new FeedResponseDto(post, distance));
        }

        //정렬 알고리즘 구현
        Comparator<FeedResponseDto> comparator = new Comparator<FeedResponseDto>() {
            @Override
            public int compare(FeedResponseDto f1, FeedResponseDto f2) {
                double distance = (f1.getDistance() - f2.getDistance());

                return (int) Math.round(distance);
            }
        };

        Collections.sort(tempFeedList, comparator);

        //sort된 List에서 페이지에 해당하는 것만 뽑아내기
        PaginationDto paginationDto = paginationService.calPage(pageable, tempFeedList);
        int startIndex = paginationDto.getStartIndex();
        int endIndex = paginationDto.getEndIndex();

        List<FeedResponseDto> feedList = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            feedList.add(tempFeedList.get(i));
        }

        return feedList;
    }

    @Transactional // 최신순 정렬
    public List<FeedResponseDto> getUserFeed(Long userId, Pageable pageable) {
        User user = userJpaRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<Post> postList = postJpaRepository.findAllByUserOrderByCreatedDateDesc(user, pageable)
                .orElse(null);
        if (postList == null)
            return null;
        return postList.stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional // 최신순 정렬
    public List<FeedResponseDto> getUserFeed(User user, Pageable pageable) {
        userJpaRepository.findById(user.getUserId()).orElseThrow(CUserNotFoundException::new);
        List<Post> postList = postJpaRepository.findAllByUserOrderByCreatedDateDesc(user, pageable)
                .orElse(null);
        if (postList == null)
            return null;
        return postList.stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }
}
