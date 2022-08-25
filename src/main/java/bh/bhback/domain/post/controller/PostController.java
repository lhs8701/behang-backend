package bh.bhback.domain.post.controller;

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


    //게시물 세부 조회(로컬 저장소)
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType="String", paramType = "header"
            )
    })
    @ApiOperation(value="게시물 조회(로컬 저장소)", notes = "게시물 조회")
    @GetMapping("/{postId}")
    public SingleResult<PostResponseDto> getPost (@PathVariable Long postId)
    {
        return responseService.getSingleResult(postService.getPost(postId));
    }


    //피드 조회(페이징 적용 - 업로드 순)
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType="String", paramType = "header"
            )
    })
    @ApiOperation(value="게시물 조회(업로드 순 정렬)", notes = "게시물 조회")
    @GetMapping("/feed")
    public ListResult<FeedResponseDto> getFeed(@PageableDefault(size=10) Pageable pageable)
    {
        return responseService.getListResult(postService.getFeed(pageable));
    }

    //유저가 올린 게시물 리스트 조회
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType="String", paramType = "header"
            )
    })
    @ApiOperation(value="유저 피드 조회", notes = "유저가 올린 게시물 리스트 조회")
    @GetMapping("/feed/{userId}")
    public ListResult<FeedResponseDto> getUserFeed(@PathVariable Long userId, @PageableDefault(size=10) Pageable pageable)
    {
        return responseService.getListResult(postService.getUserFeed(userId, pageable));
    }

    //유저가 올린 게시물 리스트 조회
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType="String", paramType = "header"
            )
    })
    @ApiOperation(value="내가 올린 피드 조회", notes = "내가 올린 게시물 리스트 조회")
    @GetMapping("/feed/me")
    public ListResult<FeedResponseDto> getMyFeed(@AuthenticationPrincipal User user, @PageableDefault(size=10) Pageable pageable)
    {
        return responseService.getListResult(postService.getUserFeed(user, pageable));
    }

    //게시물 등록
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType="String", paramType = "header"
            )
    })
    @ApiOperation(value="게시물 등록", notes = "게시물 등록")
    @PostMapping()
    public SingleResult<Long> upload (@RequestPart PostRequestDto postRequestDto, @RequestPart MultipartFile file, @AuthenticationPrincipal User user)
    {
        return responseService.getSingleResult(postService.create(postRequestDto, file, user));
    }

    //게시물 수정
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType="String", paramType = "header"
            )
    })
    @ApiOperation(value="게시물 수정", notes = "게시물 수정")
    @PatchMapping("/{postId}")
    public SingleResult<Long> update (@PathVariable Long postId, @RequestBody PostUpdateParam postUpdateParam)
    {
        return responseService.getSingleResult(postService.update(postId, postUpdateParam));
    }

    //게시물 삭제
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType="String", paramType = "header"
            )
    })
    @ApiOperation(value="게시물 삭제", notes = "게시물 삭제")
    @DeleteMapping("/{postId}")
    public CommonResult delete (@PathVariable Long postId)
    {
        postService.delete(postId);
        return responseService.getSuccessResult();
    }
}