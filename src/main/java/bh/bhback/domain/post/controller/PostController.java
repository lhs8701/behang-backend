package bh.bhback.domain.post.controller;

import bh.bhback.domain.place.dto.CurPlaceDto;
import bh.bhback.global.common.response.service.ResponseService;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.dto.ListResult;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.dto.PostUpdateParam;
import bh.bhback.domain.post.service.PostService;
import bh.bhback.domain.user.entity.User;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@Api(tags = {"Post"})
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;


    @ApiOperation(value = "게시물 조회", notes = "게시물 조회")
    @PreAuthorize("permitAll()")
    @GetMapping("/{postId}")
    public SingleResult<PostResponseDto> getPost(@PathVariable Long postId) {
        return responseService.getSingleResult(postService.getPost(postId));
    }

    //피드 조회(페이징 적용 - 업로드 순)
    @ApiOperation(value = "피드 조회(업로드 순 정렬)", notes = "메인 피드 조회")
    @PreAuthorize("permitAll()")
    @GetMapping("/feed")
    public ListResult<FeedResponseDto> getFeed(@PageableDefault(size = 10) Pageable pageable) {
        return responseService.getListResult(postService.getFeed(pageable));
    }
    @ApiOperation(value = "같은 장소에 대한 피드 조회", notes = "서브 피드 조회")
    @PreAuthorize("permitAll()")
    @GetMapping("/feed/place/{contentId}")
    public ListResult<FeedResponseDto> getFeedInSamePlace (@PathVariable Long contentId)
    {
        return responseService.getListResult(postService.getFeedInSamePlace(contentId));
    }

    @ApiOperation(value = "피드 조회(거리 순 정렬)", notes = "메인 피드 조회")
    @PostMapping("/feed/sort=Distance")
    public ListResult<FeedResponseDto> getFeedOrderByDistance(@PageableDefault(size = 10) Pageable pageable, @RequestBody CurPlaceDto curPlaceDto) {
        return responseService.getListResult(postService.getFeedOrderByDistance(pageable, curPlaceDto));
    }

    //유저가 올린 게시물 리스트 조회
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })

    @ApiOperation(value = "유저 피드 조회", notes = "해당 유저의 피드 조회")
    @PreAuthorize("permitAll()")
    @GetMapping("/feed/{userId}")
    public ListResult<FeedResponseDto> getUserFeed(@PathVariable Long userId, @PageableDefault(size = 10) Pageable pageable) {
        return responseService.getListResult(postService.getUserFeed(userId, pageable));
    }

    //내 피드 조회(페이징 적용 - 업로드 순)
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @ApiOperation(value = "내가 올린 피드 조회", notes = "나의 피드 조회")
    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/feed/me", headers = "X-AUTH-TOKEN")
    public ListResult<FeedResponseDto> getMyFeed(@AuthenticationPrincipal User user, @PageableDefault(size = 10) Pageable pageable) {
        return responseService.getListResult(postService.getUserFeed(user, pageable));
    }

    //게시물 등록
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @ApiOperation(value = "게시물 등록", notes = "게시물 등록")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(headers = "X-AUTH-TOKEN")
    public SingleResult<Long> upload(@RequestPart PostRequestDto postRequestDto, @RequestPart MultipartFile file, @AuthenticationPrincipal User user) {
        return responseService.getSingleResult(postService.create(postRequestDto, file, user));
    }

    //    게시물 수정
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @ApiOperation(value = "게시물 수정", notes = "게시물 수정")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping(value = "/{postId}", headers = "X-AUTH-TOKEN")
    public SingleResult<Long> update(@PathVariable Long postId, @RequestBody PostUpdateParam postUpdateParam, @AuthenticationPrincipal User user) {
        return responseService.getSingleResult(postService.update(postId, postUpdateParam, user));
    }

    //게시물 삭제
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @ApiOperation(value = "게시물 삭제", notes = "게시물 삭제")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(value = "/{postId}", headers = "X-AUTH-TOKEN")
    public CommonResult delete(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        postService.delete(postId, user);
        return responseService.getSuccessResult();
    }
}