package com.aacoptics.lenspacker.dashboard.service;

import com.aacoptics.lenspacker.dashboard.entity.LensInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface LensInfoService extends IService<LensInfo> {
    List<LensInfo> getLensPackerMachineInfo();
}
