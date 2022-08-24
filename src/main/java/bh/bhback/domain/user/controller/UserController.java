package bh.bhback.domain.user.controller;


import bh.bhback.domain.common.ResponseService;
import bh.bhback.domain.model.response.CommonResult;
import bh.bhback.domain.model.response.ListResult;
import bh.bhback.domain.model.response.SingleResult;
import bh.bhback.domain.user.dto.UserProfileDto;
import bh.bhback.domain.user.dto.UserResponseDto;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Api(tags = {"User"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    //회원 목록 조회
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 목록 조회", notes = "모든 회원을 조회합니다.")
    @GetMapping
    public ListResult<UserResponseDto> findAllUser() {
        return responseService.getListResult(userService.findAllUser());
    }

    //id로 회원 조회
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 검색", notes = "userId로 회원을 조회합니다.")
    @GetMapping("/id/{userId}")
    public SingleResult<UserResponseDto> findUserById
    (@ApiParam(value = "회원 ID", required = true) @PathVariable Long userId) {
        return responseService.getSingleResult(userService.findById(userId));
    }

    //회원 삭제
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "회원을 삭제합니다.")
    @DeleteMapping("/id/{userId}")
    public CommonResult delete
    (@ApiParam(value = "회원 아이디", required = true) @PathVariable Long userId) {
        userService.delete(userId);
        return responseService.getSuccessResult();
    }

    //아이디로 유저 프로필 조회
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "유저 프로필 조회", notes = "회원 아이디로 프로필을 조회합니다.")
    @GetMapping("profile/{userId}")
    public SingleResult<UserProfileDto> getUserProfile
    (@ApiParam(value = "회원 아이디", required = true) @PathVariable Long userId) {
        return responseService.getSingleResult(userService.getUserProfile(userId));
    }

    //아이디로 유저 프로필 수정
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "유저 프로필 수정", notes = "회원 아이디로 프로필을 수정합니다.")
    @PostMapping("profile/{userId}")
    public SingleResult<UserProfileDto> updateUserProfile
    (@ApiParam(value = "회원 아이디", required = true) @PathVariable Long userId,
     @ApiParam(value = "회원 수정", required = true) UserProfileDto userProfileDto
    ) {
        return responseService.getSingleResult(userService.updateUserProfile(userId, userProfileDto));
    }

}
