package bh.bhback.domain.auth.social.kakao.controller;


import bh.bhback.domain.auth.dto.UserSignupRequestDto;
import bh.bhback.domain.auth.dto.UserSocialLoginRequestDto;
import bh.bhback.domain.auth.dto.UserSocialSignupRequestDto;
import bh.bhback.domain.auth.service.AuthService;
import bh.bhback.domain.auth.social.kakao.dto.KakaoProfile;
import bh.bhback.domain.auth.social.kakao.service.KakaoApiService;
import bh.bhback.domain.auth.social.kakao.service.KakaoSignService;
import bh.bhback.domain.common.ResponseService;
import bh.bhback.domain.model.response.CommonResult;
import bh.bhback.domain.model.response.SingleResult;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.config.security.JwtProvider;
import bh.bhback.global.config.security.RefreshToken;
import bh.bhback.global.config.security.RefreshTokenJpaRepo;
import bh.bhback.global.config.security.TokenDto;
import bh.bhback.global.error.advice.exception.CUserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = {"Kakao SignUp/LogIn"})
@RequiredArgsConstructor
@RestController
public class KakaoController {

    private final ResponseService responseService;
    private final KakaoSignService kakaoSignService;

    @ApiOperation(
            value = "소셜 로그인 - kakao",
            notes = "카카오로 로그인을 합니다.")
    @PostMapping("/social/login/kakao")
    public SingleResult<TokenDto> loginByKakao(
            @ApiParam(value = "소셜 로그인 dto", required = true)
            @RequestBody UserSocialLoginRequestDto socialLoginRequestDto) {

        return responseService.getSingleResult(kakaoSignService.loginByKakao(socialLoginRequestDto));
    }

    @ApiOperation(
            value = "소셜 회원가입 - kakao",
            notes = "카카오로 회원가입을 합니다."
    )
    @PostMapping("/social/signup/kakao")
    public CommonResult signupByKakao(
            @ApiParam(value = "소셜 회원가입 dto", required = true)
            @RequestBody UserSocialSignupRequestDto socialSignupRequestDto) {

        return responseService.getSingleResult(kakaoSignService.signupByKakao(socialSignupRequestDto));
    }
}