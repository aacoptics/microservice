package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.config.MqttConfig;
import com.aacoptics.fanuc.dashboard.entity.FanucDataEntity;
import com.aacoptics.fanuc.dashboard.entity.FanucDigitalData;
import com.aacoptics.fanuc.dashboard.entity.FanucOneHourShotCountData;
import com.aacoptics.fanuc.dashboard.entity.RealFanucStatusInfo;
import com.aacoptics.fanuc.dashboard.exception.MachineNotFoundException;
import com.aacoptics.fanuc.dashboard.service.FanucDashboardService;
import com.aacoptics.fanuc.dashboard.service.FanucOneHourShotCountDataService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FanucDashboardServiceImpl implements FanucDashboardService {

    @Autowired
    FanucOneHourShotCountDataService fanucOneHourShotCountDataService;

    @Override
    public FanucDataEntity getDetailInfo(String machineName) {
        Map<String, FanucDataEntity> fanucDataMap = MqttConfig.FanucMachineDataMap;
        if (fanucDataMap != null && fanucDataMap.containsKey(machineName)) {
            return fanucDataMap.get(machineName);
        } else {
            throw new MachineNotFoundException("没有找到此机台数据:" + machineName);
        }
    }

    @Override
    public List<RealFanucStatusInfo> getByFloor() {
        List<RealFanucStatusInfo> fanucStatusInfos = new ArrayList<>();
        Map<String, FanucDataEntity> realMonitData = MqttConfig.FanucMachineDataMap;
        for (String machineName : realMonitData.keySet()) {
            RealFanucStatusInfo fanucStatusInfo = new RealFanucStatusInfo();
            JSONObject monitData = (JSONObject) realMonitData.get(machineName).getMonitData();
            if(monitData == null)
                continue;
            if (!monitData.getString("Status").equals("-1")) {
                //fanucStatusInfo.setCondMoldFileName(realMonitData.get(machineName).getMoldFileName());
                fanucStatusInfo.setMonitCycle(monitData.getString("Cycle"));
                fanucStatusInfo.setMonitCycleCount(monitData.getString("CycleCount"));
                fanucStatusInfo.setMonitShotCount(monitData.getString("ShotCount"));
                fanucStatusInfo.setMonitDateTime(monitData.getString("Date")
                        + monitData.getString("Time"));
                fanucStatusInfo.setMonitGoodCount(monitData.getString("GoodCount"));
                fanucStatusInfo.setMoldData(realMonitData.get(machineName).getMoldData());
            }
            fanucStatusInfo.setMonitStatus(monitData.getString("Status"));
            fanucStatusInfo.setMonitMcName(machineName);
            fanucStatusInfos.add(fanucStatusInfo);
        }
        return fanucStatusInfos;
    }

    @Override
    public Map<String, Map<String, Integer>> getStatusCount() {
        Map<String, Map<String, Integer>> fanucStatusCount = new HashMap<>();
        Map<String, FanucDataEntity> realMonitData = MqttConfig.FanucMachineDataMap;
        for (String machineName : realMonitData.keySet()) {
            String floor = machineName.substring(0, 2);
            if(!fanucStatusCount.containsKey(floor)){
                Map<String, Integer> statusNums = new HashMap<>();
                fanucStatusCount.put(floor, statusNums);
            }
            JSONObject monitData = (JSONObject) realMonitData.get(machineName).getMonitData();
            if(monitData == null)
                continue;
            String statusCode = monitData.getString("Status");
            if(!fanucStatusCount.get(floor).containsKey(statusCode)){
                fanucStatusCount.get(floor).put(statusCode, 0);
            }
            fanucStatusCount.get(floor).put(statusCode, fanucStatusCount.get(floor).get(statusCode) + 1);
        }
        return fanucStatusCount;
    }

    @Override
    public List<FanucDigitalData> getDigitalData() {
        List<FanucDigitalData> fanucStatusInfos = new ArrayList<>();
        Map<String, FanucDataEntity> realMonitData = MqttConfig.FanucMachineDataMap;
        List<FanucOneHourShotCountData> uph = fanucOneHourShotCountDataService.getUPH();
        for (String machineName : realMonitData.keySet()) {
            if(!machineName.startsWith("3F"))
                continue;
            FanucDigitalData fanucStatusInfo = new FanucDigitalData();
            JSONObject monitData = (JSONObject) realMonitData.get(machineName).getMonitData();
            JSONObject moldData = (JSONObject) realMonitData.get(machineName).getMoldData();
            if (!monitData.getString("Status").equals("-1")) {
                //fanucStatusInfo.setCondMoldFileName(realMonitData.get(machineName).getMoldFileName());
                fanucStatusInfo.setMonitCycle(monitData.getString("Cycle"));
                fanucStatusInfo.setMonitCycleCount(monitData.getString("CycleCount"));
                fanucStatusInfo.setMonitShotCount(monitData.getString("ShotCount"));
                fanucStatusInfo.setMonitDateTime(monitData.getString("Date")
                        + monitData.getString("Time"));
                fanucStatusInfo.setMonitGoodCount(monitData.getString("GoodCount"));
                fanucStatusInfo.setMoldAutomate(moldData.getString("mold_automate"));
                fanucStatusInfo.setMoldWait(moldData.getString("mold_wait"));
                fanucStatusInfo.setMoldManual(moldData.getString("mold_manual"));
                fanucStatusInfo.setMoldAlarm(moldData.getString("mold_alarm"));
                fanucStatusInfo.setMoldComplete(moldData.getString("mold_complete"));
                fanucStatusInfo.setMoldShutdown(moldData.getString("mold_shutdown"));
                fanucStatusInfo.setMoldTotal(moldData.getString("mold_tolal"));
                fanucStatusInfo.setMoldEnergyPerGoodShot(moldData.getString("mold_energy_per_good_shot"));
                fanucStatusInfo.setMoldEnergy(moldData.getString("mold_energy"));
                List<FanucOneHourShotCountData> machineUph = uph.stream().filter(singleUph ->
                        singleUph.getMonitMcName().equals(machineName)).collect(Collectors.toList());
                if(machineUph.size() > 0){
                    fanucStatusInfo.setUph(machineUph);
                }
            }
            fanucStatusInfo.setMonitStatus(monitData.getString("Status"));
            fanucStatusInfo.setMonitMcName(machineName);
            fanucStatusInfos.add(fanucStatusInfo);
        }
        return fanucStatusInfos;
    }

    @Override
    public Map<String, String> getCurrentOeeData() {
        Map<String, String> currentOee = new HashMap<>();
        Map<String, FanucDataEntity> realMonitData = MqttConfig.FanucMachineDataMap;
        Double totalPerCent = 0D;
        Integer machineNums = 0;
        for (String machineName : realMonitData.keySet()) {
            if(!machineName.startsWith("3F"))
                continue;
            FanucDigitalData fanucStatusInfo = new FanucDigitalData();
            JSONObject monitData = (JSONObject) realMonitData.get(machineName).getMonitData();
            JSONObject moldData = (JSONObject) realMonitData.get(machineName).getMoldData();
            if(monitData == null || moldData == null)
                continue;
            if (!monitData.getString("Status").equals("-1")) {
                totalPerCent += moldData.getString("mold_automate").equals("-") ? 0 : Double.parseDouble(moldData.getString("mold_automate").replace("%", "").trim());
                machineNums++;
            }
            DecimalFormat df=new DecimalFormat(".##");
            Double Oee = totalPerCent / machineNums;
            String OeeStr = df.format(Oee);
            currentOee.put("type", "OEE");
            currentOee.put("currentValue", OeeStr);
            currentOee.put("expectedValue", "100");
        }
        return currentOee;
    }
}