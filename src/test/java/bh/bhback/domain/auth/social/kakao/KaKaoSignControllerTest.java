package bh.bhback.domain.auth.social.kakao;

import bh.bhback.domain.auth.dto.UserSocialLoginRequestDto;
import bh.bhback.domain.auth.dto.UserSocialSignupRequestDto;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class KaKaoSignControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Environment env;

    private static String accessToken;

    @Before
    public void setUp() {
        userJpaRepository.save(User.builder()
                .nickName("tester")
                .socialId(000000L)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        accessToken = env.getProperty("social.kakao.accessToken");
    }

    @Test
    public void 카카오_회원가입_성공() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(UserSocialSignupRequestDto.builder()
                .accessToken(accessToken)
                .build());

        //when
        ResultActions actions = mockMvc.perform(
                post("/v1/social/signup/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(object)
        );

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists());
    }


    @Test
    public void 카카오_회원가입_토큰에러_실패() throws Exception
    {
        //given
        String object = objectMapper.writeValueAsString(UserSocialSignupRequestDto.builder()
                .accessToken(accessToken+"_wrongToken")
                .build());

        //when
        ResultActions actions = mockMvc.perform(
                post("/v1/social/signup/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(object));

        //then
        actions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("-1005"));
    }

    @Test
    public void 카카오_회원가입_기가입_유저_실패() throws Exception
    {
        //given
        userJpaRepository.save(User.builder()
                .nickName("tester")
                .socialId(2393625795L)
                .provider("kakao")
                .build());

        String object = objectMapper.writeValueAsString(UserSocialSignupRequestDto.builder()
                .accessToken(accessToken)
                .build());

        //when
        ResultActions actions = mockMvc.perform(post("/v1/social/signup/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("-1006"));
    }

    @Test
    public void 카카오_로그인_성공() throws Exception
    {
        //given
        String object = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
                .accessToken(accessToken)
                .build());

        //when
        mockMvc.perform(post("/v1/social/signup/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(object));

        ResultActions actions = mockMvc.perform(post("/v1/social/login/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void 카카오_로그인_액세스토큰오류_실패() throws Exception
    {
        //given
        String signUpObject = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
                .accessToken(accessToken)
                .build());
        String logInObject = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
                .accessToken(accessToken+"_wrongToken")
                .build());

        //when
        mockMvc.perform(post("/v1/social/signup/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(signUpObject));

        ResultActions actions = mockMvc.perform(post("/v1/social/login/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(logInObject));

        //then
        actions
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(-1005));
    }

    @Test
    public void 카카오_로그인_비가입자_실패() throws Exception
    {
        //given
        String logInObject = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
                .accessToken(accessToken)
                .build());

        //when
        ResultActions actions = mockMvc.perform(post("/v1/social/login/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(logInObject));

        //then
        actions
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(-1000));
    }

    @Test
    @WithMockUser(username = "mockUser", roles = {"GUEST"})
    public void 접근실패() throws Exception {
        //then
        mockMvc.perform(get("/v1/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/accessDenied"));
        ;
    }

    @Test
    @WithMockUser(username = "mockUser", roles = {"GUEST", "USER"})
    public void 접근성공() throws Exception {
        //then
        mockMvc.perform(
                        get("/v1/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}





