package bh.bhback.domain.post;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.global.common.test.RestPostService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@RunWith(SpringRunner.class)
@RestClientTest(RestPostService.class)
public class PostServiceMockTest {

//    @Autowired
//    private RestPostService postService;
//    @Autowired
//    private MockRestServiceServer server;
//
//    @Test
//    public void 회원가져오기() throws Exception {
//        //given
//        server
//                .expect(MockRestRequestMatchers
//                        .requestTo("/v1/user/email/dnstlr2933@naver.com"))
//                .andRespond(MockRestResponseCreators
//                        .withSuccess(
//                                new ClassPathResource("/test.json", getClass()),
//                                MediaType.APPLICATION_JSON)
//                );
//
//        //when
//        Post byId = postService.getPostById(1L);
//
//        //then
//        Assertions.assertThat(byId.get()).isEqualTo("dnstlr2933@naver.com");
//        Assertions.assertThat(byEmail.getName()).isEqualTo("woonsik");
//        server.verify();
//    }
}
