package com.aacoptics.mold.toollife.service;

import com.aacoptics.mold.toollife.entity.EquipInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface EquipInfoService extends IService<EquipInfo> {
    List<EquipInfo> getMachineNames();
}