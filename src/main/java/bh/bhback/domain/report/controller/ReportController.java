
package bh.bhback.domain.report.controller;

import bh.bhback.domain.report.service.ReportService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.dto.CommonResult;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.global.common.response.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @ApiOperation(
            value = "신고한다.",
            notes = "한 게시물 당 1회 가능"
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/{postId}", headers = "X-AUTH-TOKEN")
    public CommonResult reportPost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        reportService.createReport(postId, user);
        return responseService.getSuccessResult();
    }
    @ApiOperation(
            value = "신고 횟수 조회 (TEST용)",
            notes = "실제 서비스 시 삭제 예정"
    )
    @GetMapping("/{postId}")
    public SingleResult<Integer> getReportCount(@PathVariable Long postId){
        return responseService.getSingleResult(reportService.getReportCount(postId));
    }
}

