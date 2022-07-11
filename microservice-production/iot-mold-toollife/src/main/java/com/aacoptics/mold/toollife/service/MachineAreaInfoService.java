package com.aacoptics.mold.toollife.service;

import com.aacoptics.mold.toollife.entity.AreaConfig;
import com.aacoptics.mold.toollife.entity.MachineAreaInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MachineAreaInfoService extends IService<MachineAreaInfo> {

    AreaConfig getAreaConfig();
}