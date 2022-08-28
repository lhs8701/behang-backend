
package bh.bhback.domain.report.controller;

import bh.bhback.domain.report.service.ReportService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.service.ResponseService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = {"Report"})
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ResponseService responseService;

    /**
     *
     * @param postId (Report할 게시글 number)
     * @return 신고 이미 했는지 여부
     */
    @PostMapping("/{postId}")
    public CommonResult reportPost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        reportService.createReport(postId, user);
        return responseService.getSuccessResult();
    }
}

