package bh.bhback.domain.auth.social.apple.controller;

import bh.bhback.domain.auth.basic.dto.LogoutWithdrawalRequestDto;
import bh.bhback.domain.auth.social.apple.dto.AppleLoginRequestDto;
import bh.bhback.domain.auth.social.apple.service.AppleSignService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.domain.auth.jwt.dto.TokenResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"Apple Signup/Login"})
@RequestMapping("/social")
@PreAuthorize("permitAll")
public class AppleController {

    private final ResponseService responseService;
    private final AppleSignService appleSignService;

    @ApiOperation(
            value = "소셜 로그인 - apple",
            notes = "애플로 로그인을 합니다.")
    @PostMapping("/login/apple")
    public SingleResult<TokenResponseDto> loginByApple(@RequestBody @Valid AppleLoginRequestDto appleLoginRequestDto) {
        return responseService.getSingleResult(appleSignService.loginByApple(appleLoginRequestDto));
    }

    @ApiOperation(
            value = "소셜 회원가입 - apple",
            notes = "애플로 회원가입을 합니다.")
    @PostMapping("/signup/apple")
    public SingleResult<Long> signupByApple(@RequestBody @Valid AppleLoginRequestDto appleLoginRequestDto) {
        return responseService.getSingleResult(appleSignService.signupByApple(appleLoginRequestDto));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/logout/apple", headers = "X-AUTH-TOKEN")
    public CommonResult logout(@RequestHeader("X-AUTH-TOKEN") String accessToken) {

        appleSignService.logout(accessToken);
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
    @PostMapping(value = "/withdrawal/apple", headers = "X-AUTH-TOKEN")
    public CommonResult withdrawal(@RequestHeader("X-AUTH-TOKEN") String accessToken, @AuthenticationPrincipal User user) {

        appleSignService.withdrawal(accessToken, user);
        return responseService.getSuccessResult();
    }
}













