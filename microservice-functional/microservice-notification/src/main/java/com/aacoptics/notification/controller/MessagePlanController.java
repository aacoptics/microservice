package com.aacoptics.notification.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/MessagePlan")
@Api("MessagePlan")
@Slf4j
public class MessagePlanController {

    @Autowired
    PlanService planService;

    @ApiOperation(value = "查询已创建的计划", notes = "查询已创建的计划")
    @ApiImplicitParam(name = "planKey", value = "planKey", required = true, dataType = "String")
    @GetMapping("/filterPlanData")
    @CrossOrigin
    public Result filterPlanData(@RequestParam("planKey") String planKey,
                                 @RequestParam("planName") String planName) {
        return Result.success(planService.filterPlanData(planKey, planName));
    }

    @ApiOperation(value = "查询计划执行记录", notes = "查询计划执行记录")
    @ApiImplicitParam(name = "planKey", value = "planKey", required = true, dataType = "String")
    @GetMapping("/filterPlanJOB")
    @CrossOrigin
    public Result filterPlanJOB(@RequestParam("planKey") String planKey,
                                @RequestParam("planName") String planName,
                                @RequestParam("workDay") String workDay) {
        return Result.success(planService.filterPlanJOB(planKey, planName, workDay));
    }

    @ApiOperation(value = "查询联系人", notes = "查询联系人")
    @ApiImplicitParam(name = "userName", value = "userName", required = true, dataType = "String")
    @GetMapping("/filterContactData")
    @CrossOrigin
    public Result filterContactData(@RequestParam("userName") String userName) {
        return Result.success(planService.filterContactData(userName));
    }

    @ApiOperation(value = "查询计划推送人", notes = "查询计划推送人")
    @ApiImplicitParam(name = "planKey", value = "planKey", required = true, dataType = "String")
    @GetMapping("/filterPlanContact")
    @CrossOrigin
    public Result filterPlanContact(@RequestParam("planKey") String planKey) {
        return Result.success(planService.filterPlanContact(planKey));
    }

    @ApiOperation(value = "保存消息推送计划数据", notes = "保存消息推送计划数据")
    @GetMapping("/insertPlanData")
    @CrossOrigin
    public Result insertPlanData(@RequestParam Map<String, String> params) {
        try {
            String resultMsg = planService.insertPlanData(params);
            if(!"".equals(resultMsg))
            {
                return Result.fail(resultMsg);
            }
            else
            {
                return Result.success("消息推送计划保存成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("数据保存发生异常，请联系系统管理员！");
        }
    }

    @ApiOperation(value = "保存计划推送名单数据", notes = "保存计划推送名单数据")
    @PostMapping("/insertPlanContact")
    @CrossOrigin
    public Result insertPlanContact(@RequestParam("planKey") String planKey, @RequestParam("userNoList") String userNoList) {
        if(planKey == null || "".equals(planKey))
        {
            return Result.fail("计划主键不能为空！");
        }
        if(userNoList == null || "".equals(userNoList))
        {
            return Result.fail("联系人不能为空！");
        }
        String[] contactArray =  userNoList.split(",");
        try {
            String resultMsg = planService.insertPlanContact(planKey, contactArray);
            if(!"".equals(resultMsg))
            {
                return Result.fail(resultMsg);
            }
            else
            {
                return Result.success("计划推送名单保存成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("数据保存发生异常，请联系系统管理员！");
        }
    }

    @ApiOperation(value = "保存消息推送计划数据", notes = "保存消息推送计划数据")
    @GetMapping("/updatePlanData")
    @CrossOrigin
    public Result updatePlanData(@RequestParam Map<String, String> params) {
        try {
            String resultMsg = planService.updatePlanData(params);
            if(!"".equals(resultMsg))
            {
                return Result.fail(resultMsg);
            }
            else
            {
                return Result.success("消息推送计划保存成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("数据保存发生异常，请联系系统管理员！");
        }
    }

}