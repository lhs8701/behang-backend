package bh.bhback.domain.post.controller;

import bh.bhback.domain.common.ResponseService;
import bh.bhback.domain.model.response.CommonResult;
import bh.bhback.domain.model.response.ListResult;
import bh.bhback.domain.model.response.SingleResult;
import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.dto.PostUpdateParam;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.service.PostService;
import bh.bhback.domain.user.dto.UserProfileDto;
import bh.bhback.domain.user.entity.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/post")
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
    @GetMapping("/feed") //임시 url
    public ListResult<FeedResponseDto> getFeed(@PageableDefault(size=5) Pageable pageable)
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
    @GetMapping("/user/{userId}")
    public ListResult<FeedResponseDto> getUserFeed(@PathVariable Long userId)
    {
        return responseService.getListResult(postService.getUserFeed(userId));
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