package com.aacoptics.mold.toollife.service;

import com.aacoptics.mold.toollife.entity.ProgramDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ProgramDetailService extends IService<ProgramDetail> {

    List<ProgramDetail> getLastDayOee(String startTime);

    ProgramDetail getAbnormalTotalTime(String toolCode);

    List<ProgramDetail> getToolHisList(String toolCode);

    Map<String, String> getLastDayOEEByType(String type);
}