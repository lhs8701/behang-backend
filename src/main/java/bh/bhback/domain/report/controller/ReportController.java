package bh.bhback.domain.report.controller;

import bh.bhback.domain.post.dto.PostResponseDto;
import bh.bhback.domain.report.service.ReportService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.dto.SingleResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = {"Report"})
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     *
     * @param postId (Report할 게시글 number)
     * @return 신고 이미 했는지 여부
     */
    @PostMapping("/{postId}")
    public ResponseEntity<Void> reportPost(@PathVariable Long postId, @RequestBody User user) {
        //현재 유저를 어떻게 받아야 할 지 몰라서 이부분 받아서 로직에 포함해야함

        try {
            reportService.createReport(postId, user);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e) { //이미 신고했는 경우에 Exception 발생할 예정
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
    }
}
