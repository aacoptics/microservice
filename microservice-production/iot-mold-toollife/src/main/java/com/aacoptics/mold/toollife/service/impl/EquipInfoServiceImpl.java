package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.dao.EquipInfoMapper;
import com.aacoptics.mold.toollife.entity.EquipInfo;
import com.aacoptics.mold.toollife.service.EquipInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class EquipInfoServiceImpl extends ServiceImpl<EquipInfoMapper, EquipInfo> implements EquipInfoService {
    @Autowired
    EquipInfoMapper equipInfoMapper;

    @Override
    public List<EquipInfo> getMachineNames() {
        return equipInfoMapper.getMachineNames();
    }
}