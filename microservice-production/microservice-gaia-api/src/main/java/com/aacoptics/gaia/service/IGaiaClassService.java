package com.aacoptics.gaia.service;

import com.aacoptics.gaia.entity.po.GaiaClass;
import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.aacoptics.gaia.entity.vo.MessageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IGaiaClassService extends IService<GaiaClass>{

    void mergeGaiaClass(String classId, String classDesc);

    void GetClassInfoFromGaia();
}
