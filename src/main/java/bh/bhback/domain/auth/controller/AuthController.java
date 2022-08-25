package bh.bhback.domain.auth.controller;

import bh.bhback.domain.auth.service.AuthService;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.global.common.jwt.dto.TokenDto;
import bh.bhback.global.common.jwt.dto.TokenRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = {"Basic Auth"})
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;
    @ApiOperation(
            value = "액세스, 리프레시 토큰 재발급",
            notes = "엑세스 토큰 만료시 회원 검증 후 리프레쉬 토큰을 검증해서 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public SingleResult<TokenDto> reissue(
            @ApiParam(value = "토큰 재발급 요청 DTO", required = true)
            @RequestBody TokenRequestDto tokenRequestDto) {
        return responseService.getSingleResult(authService.reissue(tokenRequestDto));
    }
}
