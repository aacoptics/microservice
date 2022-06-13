package com.aacoptics.notification.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.form.XxlJobInfoQueryForm;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.service.XxlJobInfoService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/Notification")
@Api("Notification")
@Slf4j
public class NotificationController {
    @Resource
    XxlJobInfoService xxlJobInfoService;

    @ApiOperation(value = "保存消息推送计划", notes = "保存消息推送计划")
    @ApiImplicitParam(name = "xxlJobInfoForm", value = "新增消息推送计划表单", required = true, dataType = "XxlJobInfoForm")
    @GetMapping("/addNotificationTask")
    public Result addNotificationTask(@RequestParam XxlJobInfo xxlJobInfo) {
        return Result.success(xxlJobInfoService.add(xxlJobInfo));
    }

    @ApiOperation(value = "删除消息推送计划", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "消息推送计划ID", required = true, dataType = "Integer")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(xxlJobInfoService.delete(id));
    }

    @ApiOperation(value = "修改消息推送计划", notes = "修改指定消息推送计划信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "消息推送计划ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xxlJobInfoForm", value = "消息推送计划实体", required = true, dataType = "XxlJobInfoForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Integer id, @Valid @RequestBody XxlJobInfo xxlJobInfo) {
        xxlJobInfo.setId(id);
        return Result.success(xxlJobInfoService.update(xxlJobInfo));
    }

    @ApiOperation(value = "搜索消息推送计划", notes = "根据条件搜索消息推送计划信息")
    @ApiImplicitParam(name = "xxlJobInfoQueryForm", value = "消息推送计划查询参数", required = true, dataType = "XxlJobInfoQueryForm")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/conditions")
    public Result query(@Valid @RequestBody XxlJobInfoQueryForm xxlJobInfoQueryForm) {
        return Result.success(xxlJobInfoService.query(xxlJobInfoQueryForm.getPage(), xxlJobInfoQueryForm.toParam()));
    }
}