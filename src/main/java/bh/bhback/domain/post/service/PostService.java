package bh.bhback.domain.post.service;

import bh.bhback.domain.image.dto.ImageDto;
import bh.bhback.domain.image.service.ImageService;
import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.dto.PostUpdateParam;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.repository.PostJpaRepository;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.error.advice.exception.CAccessDeniedException;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import bh.bhback.global.error.advice.exception.CPostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ImageService imageService;

    @Transactional
    public Long create(PostRequestDto postRequestDto, MultipartFile file, User user) {
        ImageDto imageDto = imageService.uploadPostImage(file);
        Post post = postJpaRepository.save(postRequestDto.toEntity(user, imageDto.toEntity()));
        return post.getId();
    }

    @Transactional
    public Long update(Long postId, PostUpdateParam postUpdateParam, User user) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(CPostNotFoundException::new);
        if (!post.getUser().getUserId().equals(user.getUserId())){
            throw new CAccessDeniedException();
        }
        post.setTag(postUpdateParam.getTag());
        post.setPlace(postUpdateParam.getPlace());
        return postId;
    }

    @Transactional
    public void delete(Long postId, User user) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(CPostNotFoundException::new);
        if (!post.getUser().getUserId().equals(user.getUserId())){
            throw new CAccessDeniedException();
        }
        postJpaRepository.deleteById(postId);
    }

    @Transactional
    public PostResponseDto getPost(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(CPostNotFoundException::new);
        return new PostResponseDto(post);
    }

    @Transactional // 최신순 정렬(임시)
    public List<FeedResponseDto> getFeed(Pageable pageable) {
        List<Post> postList = postJpaRepository.findAllByOrderByCreatedDateDesc(pageable);
        return postList.stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional // 최신순 정렬(임시)
    public List<FeedResponseDto> getUserFeed(Long userId, Pageable pageable) {
        User user = userJpaRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        List<Post> postList = postJpaRepository.findAllByUserOrderByCreatedDateDesc(user, pageable);
        return postList.stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional // 최신순 정렬(임시)
    public List<FeedResponseDto> getUserFeed(User user, Pageable pageable) {
        userJpaRepository.findById(user.getUserId()).orElseThrow(CUserNotFoundException::new);
        List<Post> postList = postJpaRepository.findAllByUserOrderByCreatedDateDesc(user, pageable);
        return postList.stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }
}
