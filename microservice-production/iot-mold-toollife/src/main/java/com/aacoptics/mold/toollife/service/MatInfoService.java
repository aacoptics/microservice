package com.aacoptics.mold.toollife.service;

import com.aacoptics.mold.toollife.entity.MatInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface MatInfoService extends IService<MatInfo> {
    List<MatInfo> getMatInfo();

    Integer getScrapCount(String startTime);

    Integer getOutCount(String startTime);

    Double getScrapRate(String startTime);
}