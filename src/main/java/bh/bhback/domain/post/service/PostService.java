package bh.bhback.domain.post.service;

import bh.bhback.domain.image.dto.ImageDto;
import bh.bhback.domain.image.service.ImageService;
import bh.bhback.domain.place.dto.CurPlaceDto;
import bh.bhback.domain.place.service.PlaceService;
import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.dto.PostUpdateParam;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.repository.PostJpaRepository;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import bh.bhback.global.error.advice.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ImageService imageService;
    private final PlaceService placeService;

    @Transactional
    public Long create(PostRequestDto postRequestDto, MultipartFile file, User user) {
        String fileOriName = file.getOriginalFilename();
        String fileName = imageService.makeFileName(fileOriName);
        imageService.uploadFile(file, fileName);
        String fileUrl = imageService.makeFileUrl(fileName);
        ImageDto imageDto = new ImageDto(fileName, fileOriName, fileUrl);
        Post post = postJpaRepository.save(postRequestDto.toEntity(user, imageDto.toEntity()));
        return post.getId();
    }

    @Transactional
    public Long update(Long postId, PostUpdateParam postUpdateParam) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        post.setTag(postUpdateParam.getTag());
        post.setPlace(postUpdateParam.getPlace());
        return postId;
    }

    @Transactional
    public void delete(Long postId) {
        postJpaRepository.deleteById(postId);
    }

    @Transactional
    public PostResponseDto getPost(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        String fileUrl = post.getImage().getFileUrl();
        File file = new File(fileUrl);
        try {
            return new PostResponseDto(post, FileCopyUtils.copyToByteArray(file));
        } catch (Exception e) {
            log.info("image copyToByteArray error" + e.getMessage());
            return new PostResponseDto(post, null);
        }
    }
    @Transactional // 최신순 정렬(임시)
    public List<FeedResponseDto> getFeed(Pageable pageable) {
        List<Post> postList = postJpaRepository.findAllByOrderByCreatedDateDesc(pageable);
        List<FeedResponseDto> feedList = new ArrayList<>();
        try {
            for (Post post : postList) {
                String fileUrl = post.getImage().getFileUrl();
                File file = new File(fileUrl);
                FeedResponseDto feedResponseDto = new FeedResponseDto(post, FileCopyUtils.copyToByteArray(file));
                feedList.add(feedResponseDto);
            }
        } catch (Exception e) {
            log.info("image copyToByteArray error" + e.getMessage());
            return feedList;
        }
        return feedList;
    }

    /**
     * @param pageable
     * @param curPlaceDto (현재 위치)
     * @return 현재 부터 떨어진 거리순으로 정렬된 Feed
     */
    @Transactional
    public List<FeedResponseDto> getFeedOrderByDistance(Pageable pageable, CurPlaceDto curPlaceDto) {
        List<Post> postList = postJpaRepository.findAllByOrderByCreatedDateDesc(pageable);
        List<FeedResponseDto> feedList = new ArrayList<>();

        double curX = curPlaceDto.getCurX();
        double curY = curPlaceDto.getCurY();

        //feedList에 옮겨 담기
        try {
            for (Post post : postList) {
                //상대 거리 구하기
                double MapX = post.getPlace().getMapX();
                double MapY = post.getPlace().getMapY();

                double distance = placeService.getDistance(curX, curY, MapX, MapY);
                
                String fileUrl = post.getImage().getFileUrl();
                File file = new File(fileUrl);
                FeedResponseDto feedResponseDto = new FeedResponseDto(post, FileCopyUtils.copyToByteArray(file));
                feedList.add(feedResponseDto);
            }
        } catch (Exception e) {
            log.info("image copyToByteArray error" + e.getMessage());
            return feedList;
        }

        //정렬 알고리즘 구현
        Comparator<FeedResponseDto> comparator = new Comparator<FeedResponseDto>() {
            @Override
            public int compare(FeedResponseDto f1, FeedResponseDto f2) {
                double distance = (f1.getDistance()-f2.getDistance());

                return (int)Math.round(distance);
            }
        };

        Collections.sort(feedList, comparator);

        return feedList;
    }

    @Transactional // 최신순 정렬(임시)
    public List<FeedResponseDto> getUserFeed(Long userId, Pageable pageable) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(CUserNotFoundException::new);
//      List<Post> postList = user.getPostList();
        List<Post> postList = postJpaRepository.findAllByUserOrderByCreatedDateDesc(user, pageable);
        List<FeedResponseDto> feedList = new ArrayList<>();

        try {
            for (Post post : postList) {
                String fileUrl = post.getImage().getFileUrl();
                File file = new File(fileUrl);
                FeedResponseDto feedResponseDto = new FeedResponseDto(post, FileCopyUtils.copyToByteArray(file));
                feedList.add(feedResponseDto);
            }
        } catch (Exception e) {
            log.info("image copyToByteArray error" + e.getMessage());
            return feedList;
        }
        return feedList;
    }

    @Transactional // 최신순 정렬(임시)
    public List<FeedResponseDto> getUserFeed(User user, Pageable pageable) {
        userJpaRepository.findById(user.getUserId()).orElseThrow(CUserNotFoundException::new);

//      List<Post> postList = user.getPostList();
        List<Post> postList = postJpaRepository.findAllByUserOrderByCreatedDateDesc(user, pageable);

        List<FeedResponseDto> feedList = new ArrayList<>();
        try {
            for (Post post : postList) {
                String fileUrl = post.getImage().getFileUrl();
                File file = new File(fileUrl);
                FeedResponseDto feedResponseDto = new FeedResponseDto(post, FileCopyUtils.copyToByteArray(file));
                feedList.add(feedResponseDto);
            }
        } catch (Exception e) {
            log.info("image copyToByteArray error" + e.getMessage());
            return feedList;
        }
        return feedList;
    }
}
