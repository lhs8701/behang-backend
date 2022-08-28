package bh.bhback.domain.auth.social.apple.controller;

import bh.bhback.domain.auth.social.apple.dto.AppleSignupRequestDto;
import bh.bhback.domain.auth.social.apple.service.AppleSignService;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.global.common.jwt.dto.TokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public SingleResult<TokenDto> loginByApple(@RequestBody String refreshToken) {
        return responseService.getSingleResult(appleSignService.loginByApple(refreshToken));
    }

    @ApiOperation(
            value = "소셜 회원가입 - apple",
            notes = "애플로 회원가입을 합니다.")
    @PostMapping("/signup/apple")
    public SingleResult<String> signupByApple(@RequestBody AppleSignupRequestDto appleSignupRequestDto) {
        return responseService.getSingleResult(appleSignService.signupByApple(appleSignupRequestDto));
    }
}













