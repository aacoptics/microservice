package com.aacoptics.organization.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.organization.entity.form.MenuAccessLogForm;
import com.aacoptics.organization.entity.form.MenuForm;
import com.aacoptics.organization.entity.po.Menu;
import com.aacoptics.organization.entity.po.MenuAccessLog;
import com.aacoptics.organization.service.IMenuAccessLogService;
import com.aacoptics.organization.service.IMenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/menu")
@Api("menu")
@Slf4j
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private IMenuAccessLogService menuAccessLogService;

    @ApiOperation(value = "新增菜单", notes = "新增一个菜单")
    @ApiImplicitParam(name = "menuForm", value = "新增菜单form表单", required = true, dataType = "MenuForm")
    @PostMapping
    public Result add(@Valid @RequestBody MenuForm menuForm) {
        log.debug("name:{}", menuForm);
        Menu menu = menuForm.toPo(Menu.class);
        return Result.success(menuService.add(menu));
    }

    @ApiOperation(value = "删除菜单", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "菜单ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(menuService.delete(id));
    }

    @ApiOperation(value = "修改菜单", notes = "修改指定菜单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "menuForm", value = "菜单实体", required = true, dataType = "MenuForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody MenuForm menuForm) {
        Menu menu = menuForm.toPo(Menu.class);
        menu.setId(id);
        return Result.success(menuService.update(menu));
    }

    @ApiOperation(value = "查询所有菜单", notes = "查询所有菜单信息")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/all")
    public Result listAll() {
        return Result.success(menuService.listAll());
    }

    @ApiOperation(value = "根据菜单名查询菜单", notes = "根据菜单名查询菜单信息")
    @ApiImplicitParam(paramType = "queryByName", name = "name", value = "菜单名", required = true, dataType = "string")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/byName")
    public Result listByName(@RequestParam String name) {
        return Result.success(menuService.listByName(name));
    }

    @ApiOperation(value = "根据用户查询菜单", notes = "根据用户查询菜单信息")
    @ApiImplicitParam(paramType = "queryByUserName", name = "username", value = "用户名", required = true, dataType = "string")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/byUsername")
    public Result listByUsername(@RequestParam String username) {
        return Result.success(menuService.listByUsername(username));
    }

    @ApiOperation(value = "根据角色查询菜单", notes = "根据角色查询菜单信息")
    @ApiImplicitParam(paramType = "queryByRole", name = "roleId", value = "角色Id", required = true, dataType = "Long")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/byRole")
    public Result listByRoleId(@RequestParam Long roleId) {
        return Result.success(menuService.listByRoleId(roleId));
    }

    @ApiOperation(value = "记录菜单访问日志", notes = "记录菜单访问日志")
    @ApiImplicitParam(name = "menuForm", value = "菜单form表单", required = true, dataType = "MenuForm")
    @PostMapping(value = "/logMenuAccess")
    public Result logMenuAccess(@Valid @RequestBody MenuAccessLogForm menuAccessLogForm) {
        log.debug("name:{}", menuAccessLogForm);
        MenuAccessLog menuAccessLog = menuAccessLogForm.toPo(MenuAccessLog.class);
        return Result.success(menuAccessLogService.logMenuAccess(menuAccessLog));
    }

    @ApiOperation(value = "获取上一周菜单访问明细", notes = "获取上一周菜单访问明细")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/getAccessLogByTime")
    public Result getAccessLogByTime(@RequestParam Integer page,
                                       @RequestParam Integer size,
                                       @RequestParam String startTime,
                                       @RequestParam String endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Page<MenuAccessLog> iPage = new Page<>(page, size);
        return Result.success(menuAccessLogService.getAccessLogByTime(iPage,
                LocalDateTime.parse(startTime, df),
                LocalDateTime.parse(endTime, df)));
    }

    @ApiOperation(value = "获取上一月菜单访问次数趋势", notes = "获取上一月菜单访问次数趋势")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/getLastMouthTotalCount")
    public Result getLastMouthTotalCount() {
        return Result.success(menuAccessLogService.getLastMouthTotalCount());
    }

    @ApiOperation(value = "获取上一周菜单访问次数明细", notes = "获取上一周菜单访问次数明细")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/getLastWeekMenuCount")
    public Result getLastWeekMenuCount() {
        return Result.success(menuAccessLogService.getLastWeekMenuCount());
    }

}