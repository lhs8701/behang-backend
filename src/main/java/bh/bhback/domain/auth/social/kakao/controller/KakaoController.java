package bh.bhback.domain.auth.social.kakao.controller;


import bh.bhback.domain.auth.basic.dto.LogoutWithdrawalRequestDto;
import bh.bhback.domain.auth.social.kakao.dto.KakaoLoginRequestDto;
import bh.bhback.domain.auth.social.kakao.dto.KakaoSignupRequestDto;
import bh.bhback.domain.auth.social.kakao.service.KakaoSignService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.domain.auth.jwt.dto.TokenResponseDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = {"Kakao SignUp/LogIn"})
@RequiredArgsConstructor
@RequestMapping("/social")
@RestController
@PreAuthorize("permitAll()")
public class KakaoController {

    private final ResponseService responseService;
    private final KakaoSignService kakaoSignService;

    @ApiOperation(
            value = "소셜 로그인 - kakao",
            notes = "카카오로 로그인을 합니다.")
    @PostMapping("/login/kakao")
    public SingleResult<TokenResponseDto> loginByKakao(
            @ApiParam(value = "소셜 로그인 dto", required = true)
            @RequestBody @Valid KakaoLoginRequestDto kakaoLoginRequestDto) {

        return responseService.getSingleResult(kakaoSignService.loginByKakao(kakaoLoginRequestDto));
    }

    @ApiOperation(
            value = "소셜 회원가입 - kakao",
            notes = "카카오로 회원가입을 합니다."
    )
    @PostMapping("/signup/kakao")
    public CommonResult signupByKakao(
            @ApiParam(value = "소셜 회원가입 dto", required = true)
            @RequestBody KakaoSignupRequestDto kakaoSignupRequestDto) {
        kakaoSignService.signupByKakao(kakaoSignupRequestDto);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/logout/kakao", headers = "X-AUTH-TOKEN")
    public CommonResult logout(@RequestHeader("X-AUTH-TOKEN") String accessToken, @RequestBody LogoutWithdrawalRequestDto logoutRequestDto) {

        kakaoSignService.logout(accessToken, logoutRequestDto);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/withdrawal/kakao", headers = "X-AUTH-TOKEN")
    public CommonResult withdrawal(@RequestHeader("X-AUTH-TOKEN") String accessToken, @RequestBody LogoutWithdrawalRequestDto withdrawalRequestDto, @AuthenticationPrincipal User user) {

        kakaoSignService.withdrawal(accessToken, withdrawalRequestDto, user);
        return responseService.getSuccessResult();
    }
}