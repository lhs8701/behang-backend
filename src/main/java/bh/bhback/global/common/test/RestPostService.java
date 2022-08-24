package bh.bhback.global.common.test;

import bh.bhback.domain.post.entity.Post;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestPostService {
    private final RestTemplate restTemplate;

    public RestPostService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    public Post getPostById(Long postId){
        return restTemplate.getForObject("/posts/{postId}",Post.class, postId);
    }
}
