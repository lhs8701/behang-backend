package bh.bhback.domain.auth.social.kakao.controller;


import bh.bhback.domain.auth.dto.SocialLoginRequestDto;
import bh.bhback.domain.auth.dto.UserSocialSignupRequestDto;
import bh.bhback.domain.auth.social.kakao.service.KakaoSignService;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.global.common.jwt.dto.TokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = {"Kakao SignUp/LogIn"})
@RequiredArgsConstructor
@RequestMapping("/social")
@RestController
public class KakaoController {

    private final ResponseService responseService;
    private final KakaoSignService kakaoSignService;

    @ApiOperation(
            value = "소셜 로그인 - kakao",
            notes = "카카오로 로그인을 합니다.")
    @PostMapping("/login/kakao")
    public SingleResult<TokenDto> loginByKakao(
            @ApiParam(value = "소셜 로그인 dto", required = true)
            @RequestBody SocialLoginRequestDto socialLoginRequestDto) {

        return responseService.getSingleResult(kakaoSignService.loginByKakao(socialLoginRequestDto));
    }

    @ApiOperation(
            value = "소셜 회원가입 - kakao",
            notes = "카카오로 회원가입을 합니다."
    )
    @PostMapping("/signup/kakao")
    public CommonResult signupByKakao(
            @ApiParam(value = "소셜 회원가입 dto", required = true)
            @RequestBody UserSocialSignupRequestDto socialSignupRequestDto) {

        return responseService.getSingleResult(kakaoSignService.signupByKakao(socialSignupRequestDto));
    }
}