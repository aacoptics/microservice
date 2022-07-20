package com.aacoptics.lenspacker.dashboard.service.impl;

import com.aacoptics.lenspacker.dashboard.dao.LensPackerOneHourCapacityMapper;
import com.aacoptics.lenspacker.dashboard.entity.*;
import com.aacoptics.lenspacker.dashboard.service.LensPackerOneHourCapacityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class LensPackerOneHourCapacityServiceImpl extends ServiceImpl<LensPackerOneHourCapacityMapper, LensPackerOneHourCapacity> implements LensPackerOneHourCapacityService {
    @Autowired
    private LensPackerOneHourCapacityMapper lensPackerOneHourCapacityMapper;

    @Override
    public List<LensPackerOneHourCapacity> getTotalUph() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd 08:00:00");
        if (currentTime.getHour() < 8) {
            currentTime = currentTime.plusDays(-1);
        }
        String startTime = df.format(currentTime);
        return lensPackerOneHourCapacityMapper.getTotalUph(startTime);
    }

    @Override
    public List<LensPackerOneHourCapacity> getMachineCapacity(String startTime, String endTime){
        return lensPackerOneHourCapacityMapper.getMachineCapacity(startTime, endTime);
    }
}
