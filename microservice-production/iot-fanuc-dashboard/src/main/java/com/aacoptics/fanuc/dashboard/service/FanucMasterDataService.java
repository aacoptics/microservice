package com.aacoptics.fanuc.dashboard.service;

import com.aacoptics.fanuc.dashboard.entity.FanucMasterData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FanucMasterDataService extends IService<FanucMasterData> {
    List<String> selectEquipNames();
}
