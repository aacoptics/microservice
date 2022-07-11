package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.dao.ProgramDetailMapper;
import com.aacoptics.mold.toollife.entity.ProgramDetail;
import com.aacoptics.mold.toollife.service.ProgramDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ProgramDetailServiceImpl extends ServiceImpl<ProgramDetailMapper, ProgramDetail> implements ProgramDetailService {

    @Autowired
    ProgramDetailMapper programDetailMapper;

    @Override
    public List<ProgramDetail> getLastDayOee(String startTime) {
        return programDetailMapper.getLastDayOee(startTime);
    }

    @Override
    public ProgramDetail getAbnormalTotalTime(String toolCode) {
        return programDetailMapper.getAbnormalTotalTime(toolCode);
    }

    @Override
    public List<ProgramDetail> getToolHisList(String toolCode){
        return programDetailMapper.getToolHisList(toolCode);
    }

    @Override
    public Map<String, String> getLastDayOEEByType(String type) {
        Map<String, String> oee = new HashMap<>();
        Date now = new Date();
        Date yesterday = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        yesterday = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayDate = sdf.format(yesterday) + " 07:30:00";
        String oeeValue = programDetailMapper.getLastDayOEEByType(yesterdayDate, type);
        oee.put("type", type + " OEE");
        oee.put("currentValue", oeeValue);
        oee.put("expectedValue", "100");


        return oee;
    }
}