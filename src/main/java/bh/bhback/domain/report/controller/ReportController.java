package bh.bhback.domain.report.controller;

import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.dto.CommonResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/report")
public class ReportController {

//    public CommonResult doReport(Long postId, @AuthenticationPrincipal User user){
//
//    }

}
