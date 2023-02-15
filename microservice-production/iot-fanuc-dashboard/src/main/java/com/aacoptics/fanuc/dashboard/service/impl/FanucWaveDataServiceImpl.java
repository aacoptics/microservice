package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.dao.FanucMasterDataMapper;
import com.aacoptics.fanuc.dashboard.dao.FanucWaveDataMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucMasterData;
import com.aacoptics.fanuc.dashboard.entity.po.FanucWaveData;
import com.aacoptics.fanuc.dashboard.service.FanucMasterDataService;
import com.aacoptics.fanuc.dashboard.service.FanucWaveDataService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@DS("waveDB")
public class FanucWaveDataServiceImpl extends ServiceImpl<FanucWaveDataMapper, FanucWaveData> implements FanucWaveDataService {

    @Resource
    FanucWaveDataMapper fanucWaveDataMapper;

    @Override
    public List<Integer> selectCycleNos(LocalDateTime startTime, LocalDateTime endTime, String machineNo) {
        LambdaQueryWrapper<FanucWaveData> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(FanucWaveData::getCycleCount)
                .eq(FanucWaveData::getMachineNo, machineNo)
                .between(FanucWaveData::getStartTime, startTime, endTime);
        return fanucWaveDataMapper.selectObjs(lambdaQueryWrapper).stream()
                .map(o -> (Integer) o)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, JSONArray> getFanucWaveData(List<Integer> cycleNos) {
        List<FanucWaveData> fanucWaveDataList = fanucWaveDataMapper.getFanucWaveData(cycleNos);

        JSONArray injectPressureJson = new JSONArray();
        JSONArray analogInput1Json = new JSONArray();
//        String tempWaferId = "0";
//        LocalDateTime minDateTime = LocalDateTime.MIN;
        JSONArray firstArray = new JSONArray();
        firstArray.add("cycleCount");
        firstArray.add("paramValue");
        firstArray.add("timeStamp");
        injectPressureJson.add(firstArray);
        analogInput1Json.add(firstArray);

        for (FanucWaveData fanucWaveData : fanucWaveDataList) {
            JSONArray singleArray = new JSONArray();
            singleArray.add(fanucWaveData.getCycleCount().toString());
            singleArray.add(fanucWaveData.getInjectPressure());
            singleArray.add(fanucWaveData.getTimeStamp());
            injectPressureJson.add(singleArray);

            JSONArray singleArray2 = new JSONArray();
            singleArray2.add(fanucWaveData.getCycleCount());
            singleArray2.add(fanucWaveData.getAnalogInput1());
            singleArray2.add(fanucWaveData.getTimeStamp());
            analogInput1Json.add(singleArray2);
        }

        Map<String, JSONArray> fanucWaveDataMap = new HashMap<>();
        fanucWaveDataMap.put("injectPressure", injectPressureJson);
        fanucWaveDataMap.put("analogInput1", analogInput1Json);

        return fanucWaveDataMap;
    }
}
