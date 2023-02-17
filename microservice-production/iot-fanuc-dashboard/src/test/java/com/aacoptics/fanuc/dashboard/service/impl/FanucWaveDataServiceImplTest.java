package com.aacoptics.fanuc.dashboard.service.impl;

import com.aacoptics.fanuc.dashboard.entity.po.FanucWaveData;
import com.aacoptics.fanuc.dashboard.service.FanucWaveDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FanucWaveDataServiceImplTest {

    @Resource
    FanucWaveDataService fanucWaveDataService;

    @Test
    public void selectCycleNos() {
        LocalDateTime startTime = LocalDateTime.of(2023, 2, 15, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 2, 16, 0, 0, 0);
        List<Integer> test = fanucWaveDataService.selectCycleNos(startTime, endTime, "4FM01");
        String test1 = "";
    }

    @Test
    public void getFanucWaveData() {
        List<Integer> cycleNos = Arrays.asList(35633, 35634, 35635, 35636, 35637, 35638);
        Map<Integer, List<FanucWaveData>> test = fanucWaveDataService.getFanucWaveData(cycleNos);
        String test1 = "";
    }
}