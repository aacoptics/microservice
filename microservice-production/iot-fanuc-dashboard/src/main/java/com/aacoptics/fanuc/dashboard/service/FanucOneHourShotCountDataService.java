package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucOneHourShotCountData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FanucOneHourShotCountDataService extends IService<FanucOneHourShotCountData> {
    List<FanucOneHourShotCountData> getUPH();

    List<FanucOneHourShotCountData> getTotalUph();


}
