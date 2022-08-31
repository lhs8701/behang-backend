package bh.bhback.domain.place.controller;

import bh.bhback.domain.place.service.PlaceService;
import bh.bhback.domain.post.dto.FeedResponseDto;
import bh.bhback.domain.post.dto.PostRequestDto;
import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.global.common.response.dto.ListResult;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.global.common.response.service.ResponseService;
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
    private final ResponseService responseService;


}
