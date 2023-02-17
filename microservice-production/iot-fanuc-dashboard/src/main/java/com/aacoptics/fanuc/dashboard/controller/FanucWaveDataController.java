package com.aacoptics.fanuc.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.fanuc.dashboard.entity.param.FanucCheckDataEveryDayParam;
import com.aacoptics.fanuc.dashboard.entity.param.FanucWaveDataParam;
import com.aacoptics.fanuc.dashboard.service.FanucWaveDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;

@RestController
@RequestMapping("/fanucWaveData")
@Api("fanucWaveData")
@Slf4j
public class FanucWaveDataController {
    @Resource
    FanucWaveDataService fanucWaveDataService;

    @ApiOperation(value = "获取指定时间的模次号", notes = "获取指定时间的模次号")
    @PostMapping("/getCycleNosByTime")
    public Result getCycleNosByTime(@RequestBody FanucWaveDataParam fanucWaveDataParam) {
        return Result.success(fanucWaveDataService.selectCycleNos(fanucWaveDataParam.getStartTime(), fanucWaveDataParam.getEndTime(), fanucWaveDataParam.getMachineNo()));
    }

    @ApiOperation(value = "获取指定模次的波形", notes = "获取指定模次的波形")
    @PostMapping("/getWaveDataByCycleNo")
    public Result getWaveDataByCycleNo(@RequestBody FanucWaveDataParam fanucWaveDataParam) {
        return Result.success(fanucWaveDataService.getFanucWaveData(fanucWaveDataParam.getCycleNos()));
    }

    @PostMapping("/downloadExcel")
    @ApiOperation(value = "下载Excel", notes = "下载Excel")
    public void downloadLocal(@RequestBody FanucWaveDataParam fanucWaveDataParam, HttpServletResponse response) {
        OutputStream os = null;
        InputStream is = null;
        try {
            File file = fanucWaveDataService.exportExcel(fanucWaveDataParam.getCycleNos());
            log.info(file.getPath());
            String filename = file.getName();
            os = response.getOutputStream();
            response.reset();
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
            is = Files.newInputStream(file.toPath());
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
    }
}