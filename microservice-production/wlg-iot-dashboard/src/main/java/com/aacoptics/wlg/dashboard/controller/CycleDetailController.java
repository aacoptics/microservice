package com.aacoptics.wlg.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.dashboard.entity.param.CycleDetailParam;
import com.aacoptics.wlg.dashboard.entity.po.CycleDetail;
import com.aacoptics.wlg.dashboard.service.CycleDetailService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/CycleDetail")
@Api("CycleDetail")
@Slf4j
public class CycleDetailController {

    @Resource()
    CycleDetailService cycleDetailService;


    @ApiOperation(value = "修改信息", notes = "修改指定信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "cycleDetail", value = "实体", required = true, dataType = "CycleDetail")})
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody CycleDetail cycleDetail) {
        cycleDetail.setId(id);
        return Result.success(cycleDetailService.update(cycleDetail));
    }

    @ApiOperation(value = "搜索", notes = "根据条件查询信息")
    @ApiImplicitParam(name = "cycleDetailParam", value = "查询参数", required = true, dataType = "CycleDetailParam")
    @ApiResponses(@ApiResponse(code = 200, message = "处理成功", response = Result.class))
    @PostMapping(value = "/conditions")
    public Result search(@Valid @RequestBody CycleDetailParam cycleDetailParam) {
        log.debug("search with cycleDetailParam:{}", cycleDetailParam);
        return Result.success(cycleDetailService.query(cycleDetailParam.getPage(), cycleDetailParam));
    }
}