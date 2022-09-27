package com.aacoptics.notification.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.form.RobotForm;
import com.aacoptics.notification.entity.form.RobotQueryForm;
import com.aacoptics.notification.entity.param.RobotQueryParam;
import com.aacoptics.notification.entity.po.Robot;
import com.aacoptics.notification.service.RobotService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/robot")
@Api("robot")
@Slf4j
public class RobotController {

    @Resource
    private RobotService robotService;

    @ApiOperation(value = "新增机器人", notes = "新增一个机器人")
    @ApiImplicitParam(name = "robotForm", value = "新增机器人form表单", required = true, dataType = "RobotForm")
    @PostMapping
    public Result add(@Valid @RequestBody RobotForm robotForm) {
        log.debug("name:{}", robotForm);
        Robot robot = robotForm.toPo(Robot.class);
        return Result.success(robotService.add(robot));
    }

    @ApiOperation(value = "删除机器人", notes = "根据url的id来指定删除对象，逻辑删除")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(robotService.delete(id));
    }

    @ApiOperation(value = "修改机器人", notes = "修改指定机器人信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "机器人ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "userUpdateForm", value = "机器人实体", required = true, dataType = "UserUpdateForm")})
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody RobotForm robotForm) {
        Robot user = robotForm.toPo(Robot.class);
        user.setId(id);
        return Result.success(robotService.update(user));
    }

    @ApiOperation(value = "搜索机器人", notes = "根据条件查询机器人信息")
    @ApiImplicitParam(name = "userQueryForm", value = "机器人查询参数", required = true, dataType = "UserQueryForm")
    @ApiResponses(@ApiResponse(code = 200, message = "处理成功", response = Result.class))
    @PostMapping(value = "/conditions")
    public Result search(@Valid @RequestBody RobotQueryForm robotQueryForm) {
        log.debug("search with robotQueryForm:{}", robotQueryForm);
        return Result.success(robotService.query(robotQueryForm.getPage(), robotQueryForm.toParam(RobotQueryParam.class)));
    }

    @ApiOperation(value = "获取所有机器人", notes = "获取所有机器人")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/listAll")
    public Result listGroup() {
        return Result.success(robotService.listAll());
    }

    @ApiOperation(value = "根据名称搜索机器人", notes = "根据名称搜索机器人")
    @ApiResponses(@ApiResponse(code = 200, message = "处理成功", response = Result.class))
    @PostMapping(value = "/findByNames")
    public Result findByNames(@RequestBody List<String> robotNames) {
        return Result.success(robotService.findByName(robotNames));
    }

    @ApiOperation(value = "根据id搜索机器人", notes = "根据id搜索机器人")
    @ApiResponses(@ApiResponse(code = 200, message = "处理成功", response = Result.class))
    @PostMapping(value = "/findByIds")
    public Result findByIds(@RequestBody List<Long> ids) {
        return Result.success(robotService.findByIds(ids));
    }
}