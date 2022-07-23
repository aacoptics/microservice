package com.aacoptics.fanuc.dashboard.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.fanuc.dashboard.dao.FanucEnergyDataMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucEnergyData;
import com.aacoptics.fanuc.dashboard.service.FanucDashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/fanucDigital")
@Api("fanuc")
@Slf4j
public class FanucDigitalController {
    @Resource
    FanucDashboardService fanucDashboardService;

    @Resource
    FanucEnergyDataMapper fanucEnergyDataMapper;

    @ApiOperation(value = "实时状态数据", notes = "查询实时状态数据")
    @GetMapping("/getDigitalData")
    public Result getDigitalData() {
        return Result.success(fanucDashboardService.getDigitalData());
    }


    @ApiOperation(value = "实时OEE数据", notes = "查询实时OEE数据")
    @GetMapping("/getCurrentOee")
    public Result getCurrentOee() {
        return Result.success(fanucDashboardService.getCurrentOeeData());
    }

    @ApiOperation(value = "上月的能源情况", notes = "上月的能源情况")
    @GetMapping("/getLastMouthEnergy")
    public Result getLastMouthEnergy() {
        return Result.success(fanucEnergyDataMapper.getLastMouthEnergy());
    }

    @ApiOperation(value = "当前能源", notes = "当前能源")
    @GetMapping("/getCurrentEnergy")
    public Result getCurrentEnergy() {

        List<FanucEnergyData> res = fanucEnergyDataMapper.getCurrentEnergy();
        if(res.size() > 0)
            return Result.success(res.get(0).getEnergy());
//        Map<String, FanucDataEntity> realMonitData = MqttConfig.FanucMachineDataMap;
//        float energy = 0f;
//        for (String machineName : realMonitData.keySet()) {
//            if(!machineName.startsWith("3F"))
//                continue;
//            JSONObject moldData = (JSONObject) realMonitData.get(machineName).getMoldData();
//            if(moldData != null && !StringUtil.isBlank(moldData.getString("mold_energy"))){
//                try {
//                    float machineEnergy = Float.parseFloat(moldData.getString("mold_energy").replace("KWh", ""));
//                    energy += machineEnergy;
//                }catch(Exception err){
//
//                }
//            }
//        }
        else
            return Result.success(0);
    }

}
