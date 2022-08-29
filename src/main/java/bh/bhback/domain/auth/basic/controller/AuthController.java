package bh.bhback.domain.auth.basic.controller;

import bh.bhback.domain.auth.basic.service.AuthService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.domain.auth.jwt.dto.TokenResponseDto;
import bh.bhback.domain.auth.jwt.dto.TokenRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = {"Basic Auth"})
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;

    @ApiOperation(value = "액세스, 리프레시 토큰 재발급", notes = "엑세스 토큰 만료시 회원 검증 후 리프레쉬 토큰을 검증해서 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @PreAuthorize("permitAll()")
    @PostMapping(value = "/reissue")
    public SingleResult<TokenResponseDto> reissue(
            @ApiParam(value = "토큰 재발급 요청 DTO", required = true)
            @RequestBody TokenRequestDto tokenRequestDto) {
        return responseService.getSingleResult(authService.reissue(tokenRequestDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/logout", headers = "X-AUTH-TOKEN")
    public CommonResult logout(@RequestHeader("X-AUTH-TOKEN") String accessToken) {

        authService.logout(accessToken);
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
    @PostMapping(value = "/withdrawal", headers = "X-AUTH-TOKEN")
    public CommonResult withdrawal(@RequestHeader("X-AUTH-TOKEN") String accessToken, @AuthenticationPrincipal User user) {

        authService.withdrawal(accessToken, user);
        return responseService.getSuccessResult();
    }
}
