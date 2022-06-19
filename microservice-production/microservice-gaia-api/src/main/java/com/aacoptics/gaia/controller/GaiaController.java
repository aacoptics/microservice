package com.aacoptics.gaia.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.aacoptics.gaia.exception.CommonErrorType;
import com.aacoptics.gaia.service.IPlanActualPerPersonService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 数据字典类型表
 *
 * @author yanshangqi
 */
@RestController
@RequestMapping("/gaiaApi")
@Api("CustomerShipmentInfoController")
@Slf4j
public class GaiaController {

    @Resource
    IPlanActualPerPersonService planActualPerPersonService;

    @ApiOperation(value = "获取排班数据", notes = "获取排班数据")
    @GetMapping("/getActualPlanInfo")
    public Result getActualPlanInfo(@RequestParam String startDate,
                                    @RequestParam String endDate) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startTime;
        LocalDateTime endTime;
        try {
            startTime = LocalDate.parse(startDate, df).atStartOfDay();
            endTime = LocalDate.parse(endDate, df).atStartOfDay();
        } catch (Exception err) {
            return Result.fail(new CommonErrorType("050000", "日期格式不正确，请检查！"));
        }
        return Result.success(planActualPerPersonService.getPlanInfoByTime(startTime, endTime));
    }

    @ApiOperation(value = "返回排班结果信息", notes = "返回排班结果信息")
    @PostMapping("/updatePlanResult")
    public Result updatePlanResult(@RequestBody List<PlanActualPerPerson> planActualPerPersons) {
        try {
            planActualPerPersonService.updatePlanResult(planActualPerPersons);
            return Result.success();
        } catch (Exception err) {
            return Result.fail(err);
        }
    }
}
