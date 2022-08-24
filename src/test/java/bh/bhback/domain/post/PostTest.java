package bh.bhback.domain.post;

import bh.bhback.domain.image.dto.ImageDto;
import bh.bhback.domain.image.entity.Image;
import bh.bhback.domain.image.repository.ImageJpaRepository;
import bh.bhback.domain.image.service.ImageService;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.entity.Place;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.entity.Tag;
import bh.bhback.domain.post.repository.PostJpaRepository;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.error.advice.exception.PostNotFoundException;
import org.assertj.core.api.Assertions;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PostTest {
    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ImageService imageService;

    private User user1, user2;

    @BeforeEach
    public void before(){
        User user1 = User.builder()
                .socialId(0L)
                .nickName("user1")
                .profileImage("user1-image")
                .provider("test")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        User user2 = User.builder()
                .socialId(1L)
                .nickName("user2")
                .profileImage("user2-image")
                .provider("test")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userJpaRepository.save(user1);
        userJpaRepository.save(user2);
    }

    @Test
    public void 포스트_생성_후_id로_조회() throws Exception {
        //given
        ImageDto imageDto = new ImageDto("a","b","c");
        Image image = imageService.save(imageDto);
//        Image image = imageDto.toEntity();
        Post savedPost = postJpaRepository.save(Post.builder()
                .user(user1)
                .image(image)
                .tag(new Tag())
                .place(new Place())
                .build());

        //when
        Post findedPost = postJpaRepository.findById(savedPost.getId())
                .orElseThrow(PostNotFoundException::new);

        //then
        assertThat(savedPost).isEqualTo(findedPost);
    }

}