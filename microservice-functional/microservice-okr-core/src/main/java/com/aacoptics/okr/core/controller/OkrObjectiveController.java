package com.aacoptics.okr.core.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.okr.core.entity.po.AlignRelation;
import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;
import com.aacoptics.okr.core.service.AlignRelationService;
import com.aacoptics.okr.core.service.KeyResultDetailService;
import com.aacoptics.okr.core.service.ObjectiveDetailService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/aacOkrObjective")
@Api("aacOkrObjective")
@Slf4j
public class OkrObjectiveController {
    @Resource
    private ObjectiveDetailService objectiveDetailService;

    @Resource
    private KeyResultDetailService keyResultDetailService;

    @Resource
    AlignRelationService alignRelationService;

    @ApiOperation(value = "新增OKR", notes = "新增OKR")
    @ApiImplicitParam(name = "objectiveDetail", value = "新增OKR表单", required = true, dataType = "ObjectiveDetail")
    @PostMapping
    public Result add(@Valid @RequestBody ObjectiveDetail objectiveDetail) {
        return Result.success(objectiveDetailService.add(objectiveDetail));
    }

    @ApiOperation(value = "删除OKR", notes = "根据url的id来指定删除OKR")
    @ApiImplicitParam(paramType = "path", name = "id", value = "OKR ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteObjective/{id}")
    public Result deleteObjective(@PathVariable Long id) {
        return Result.success(objectiveDetailService.deleteObjective(id));
    }

    @ApiOperation(value = "删除KR", notes = "根据url的id来指定删除KR")
    @ApiImplicitParam(paramType = "path", name = "id", value = "KR ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/deleteKeyResult/{id}")
    public Result deleteKeyResult(@PathVariable Long id) {
        return Result.success(keyResultDetailService.deleteKeyResult(id));
    }
    @ApiOperation(value = "修改OKR", notes = "修改指定OKR")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "OKRID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "objectiveDetail", value = "OKR实体", required = true, dataType = "ObjectiveDetail")})
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody ObjectiveDetail objectiveDetail) {
        objectiveDetail.setId(id);
        return Result.success(objectiveDetailService.update(objectiveDetail));
    }

    @ApiOperation(value = "获取所有OKR周期", notes = "获取所有OKR周期")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/listAllByUsername")
    public Result listAllByUsername(@RequestParam Long periodId, @RequestParam String username) {
        return Result.success(objectiveDetailService.listAllByUsername(username, periodId));
    }

    @ApiOperation(value = "获取所有OKR周期", notes = "获取所有OKR周期")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/listAllByUsernameIncludeAlignInfo")
    public Result listAllByUsernameIncludeAlignInfo(@RequestParam Long periodId, @RequestParam String username, @RequestParam Long objectiveId) {
        return Result.success(objectiveDetailService.listAllByUsername(username, periodId, objectiveId));
    }

    @ApiOperation(value = "更新O状态，分值", notes = "更新O状态，分值")
    @ApiImplicitParam(name = "objectiveDetail", value = "新增OKR表单", required = true, dataType = "ObjectiveDetail")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PutMapping(value = "/updateObjectiveStatusAndScore")
    public Result updateObjectiveStatusAndScore(@RequestBody ObjectiveDetail objectiveDetail) {
        Boolean res = objectiveDetailService.updateStatusAndScore(objectiveDetail);
        return res? Result.success() : Result.fail("更新失败！");
    }

    @ApiOperation(value = "更新备注", notes = "更新备注")
    @ApiImplicitParam(name = "objectiveDetail", value = "新增OKR表单", required = true, dataType = "ObjectiveDetail")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PutMapping(value = "/updateObjectiveRemark")
    public Result updateObjectiveRemark(@RequestBody ObjectiveDetail objectiveDetail) {
        Boolean res = objectiveDetailService.updateRemark(objectiveDetail);
        return res? Result.success() : Result.fail("更新失败！");
    }

    @ApiOperation(value = "更新KR状态，分值", notes = "更新KR状态，分值")
    @ApiImplicitParam(name = "keyResultDetail", value = "新增OKR表单", required = true, dataType = "KeyResultDetail")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PutMapping(value = "/updateKrStatusAndScore")
    public Result updateKrStatusAndScore(@RequestBody KeyResultDetail keyResultDetail) {
        Boolean res = keyResultDetailService.updateStatusAndScore(keyResultDetail);
        return res? Result.success() : Result.fail("更新失败！");
    }

    @ApiOperation(value = "更新或插入Objective", notes = "更新或插入Objective")
    @ApiImplicitParam(name = "objectiveDetail", value = "新增OKR表单", required = true, dataType = "ObjectiveDetail")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PutMapping(value = "/addOrUpdateObjective")
    public Result addOrUpdateObjective(@RequestBody ObjectiveDetail objectiveDetail) {
        Boolean res = objectiveDetailService.addOrUpdateObjective(objectiveDetail);
        return res? Result.success() : Result.fail("更新失败！");
    }

    @ApiOperation(value = "获取人员OKRTree", notes = "获取人员OKRTree")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @GetMapping(value = "/searchUserObjective")
    public Result searchUserObjective(@RequestParam Long periodId,
                                      @RequestParam String userInfo,
                                      @RequestParam String currentUsername,
                                      @RequestParam Long objectiveId) {
        return Result.success(objectiveDetailService.getUserObjectiveTree(userInfo, periodId, currentUsername, objectiveId));
    }

    @ApiOperation(value = "对齐目标", notes = "对齐目标")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = Result.class)
    )
    @PostMapping(value = "/alignOkr")
    public Result alignOkr(@RequestBody AlignRelation alignRelation) {
        return Result.success(alignRelationService.add(alignRelation));
    }

    @ApiOperation(value = "取消对齐", notes = "取消对齐")
    @DeleteMapping(value = "/removeAlignInfo")
    public Result removeAlignInfo(@RequestParam Integer alignType,
                                  @RequestParam Long ObjectiveId,
                                  @RequestParam Long alignId) {
        return Result.success(alignRelationService.deleteAlignInfo(alignType, ObjectiveId, alignId));
    }
}