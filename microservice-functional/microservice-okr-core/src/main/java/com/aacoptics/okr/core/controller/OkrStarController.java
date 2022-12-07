package com.aacoptics.okr.core.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.okr.core.service.StarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Kaizhi Xuan
 * created on 2022/12/7 15:39
 */
@RestController
@RequestMapping("/aacOkrStar")
@Api("aacOkrStar")
@Slf4j
public class OkrStarController {

    @Resource
    private StarService starService;

    @ApiOperation(value = "是否关注", notes = "是否关注")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/isStar")
    public Result<Boolean> isStar(@RequestParam String userName,
                                  @RequestParam String starUserName) {
        return Result.success(starService.isStar(userName, starUserName));
    }

    @ApiOperation(value = "创建和删除", notes = "创建和删除")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/deleteOrCreate")
    public Result<Boolean> deleteOrCreate(@RequestParam String userName,
                                          @RequestParam String starUserName) {
        return Result.success(starService.deleteOrCreate(userName, starUserName));
    }

}
