package bh.bhback.domain.history.controller;

import bh.bhback.domain.history.dto.AreaInfoDto;
import bh.bhback.domain.history.service.HistoryService;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.common.response.dto.ListResult;
import bh.bhback.global.common.response.dto.SingleResult;
import bh.bhback.global.common.response.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"History"})
@RequestMapping("/history")
public class HistoryController {

    private final ResponseService responseService;
    private final HistoryService historyService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "AccessToken",
                    required = true, dataType = "String", paramType = "header"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping(headers = "X-AUTH-TOKEN")
    public ListResult<AreaInfoDto> getThumbnail(@AuthenticationPrincipal User user){
        return responseService.getListResult(historyService.getThumbnail(user));
    }
}
