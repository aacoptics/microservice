package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.dao.FanucMasterDataMapper;
import com.aacoptics.fanuc.dashboard.dao.FanucWaveDataMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucMasterData;
import com.aacoptics.fanuc.dashboard.entity.po.FanucWaveData;
import com.aacoptics.fanuc.dashboard.service.FanucMasterDataService;
import com.aacoptics.fanuc.dashboard.service.FanucWaveDataService;
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
    public Map<Integer, List<FanucWaveData>> getFanucWaveData(List<Integer> cycleNos) {
        List<FanucWaveData> fanucWaveDataList = fanucWaveDataMapper.getFanucWaveData(cycleNos);
        Map<Integer, List<FanucWaveData>> fanucWaveDataMap = new HashMap<>();
        for (Integer cycleNo : cycleNos) {
            fanucWaveDataMap.put(cycleNo, fanucWaveDataList.stream()
                    .filter(fanucWaveData -> fanucWaveData.getCycleCount().equals(cycleNo))
                    .collect(Collectors.toList()));
        }
        return fanucWaveDataMap;
    }
}
