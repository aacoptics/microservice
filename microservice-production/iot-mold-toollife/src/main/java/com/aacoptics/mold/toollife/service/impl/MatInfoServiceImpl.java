package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.dao.MatInfoMapper;
import com.aacoptics.mold.toollife.entity.MatInfo;
import com.aacoptics.mold.toollife.service.MatInfoService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;


@Service
@Slf4j
@DS("moldMes")
public class MatInfoServiceImpl extends ServiceImpl<MatInfoMapper, MatInfo> implements MatInfoService {
    @Autowired
    MatInfoMapper matInfoMapper;

    @Override
    public List<MatInfo> getMatInfo() {
        return matInfoMapper.getMatInfo();
    }

    @Override
    public Integer getScrapCount(String startTime) {
        return matInfoMapper.getScrapCount(startTime);
    }

    @Override
    public Integer getOutCount(String startTime) {
        return matInfoMapper.getOutCount(startTime);
    }

    @Override
    public Double getScrapRate(String startTime) {
        DecimalFormat df = new DecimalFormat("0.00");
        Integer scrapCount = getScrapCount(startTime);
        Integer outCount = getOutCount(startTime);
        return Double.valueOf(df.format((float)scrapCount/outCount));
    }
}