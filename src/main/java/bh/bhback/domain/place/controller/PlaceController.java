package bh.bhback.domain.place.controller;

import bh.bhback.domain.place.service.PlaceService;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.global.common.response.dto.SingleResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = {"Place"})
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/{contentId}")
    public ResponseEntity<List<PostResponseDto>> getPostByContentId (@PathVariable Long contentId)
    {
        try {
            List<PostResponseDto> postResponseDtoList = placeService.getPostListByContentId(contentId);

            return ResponseEntity.ok().body(postResponseDtoList);
        }catch(Exception e) {
            
            //body 없는 notFound ResponseEntity 반환
            return ResponseEntity.notFound().build();
        }
    }
}
