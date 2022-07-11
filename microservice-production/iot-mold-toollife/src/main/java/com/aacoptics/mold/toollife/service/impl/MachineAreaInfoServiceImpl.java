package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.dao.MachineAreaInfoMapper;
import com.aacoptics.mold.toollife.entity.AreaConfig;
import com.aacoptics.mold.toollife.entity.MachineAreaInfo;
import com.aacoptics.mold.toollife.service.MachineAreaInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MachineAreaInfoServiceImpl extends ServiceImpl<MachineAreaInfoMapper, MachineAreaInfo> implements MachineAreaInfoService {

    @Override
    public AreaConfig getAreaConfig() {
        AreaConfig areaConfig = new AreaConfig();
        Map<String, String> areaInfo = new HashMap<>();
        Set<String> areaCode = new HashSet<>();
        List<MachineAreaInfo> machineAreaInfos = this.list();
        for (MachineAreaInfo machineAreaInfo : machineAreaInfos) {
            areaInfo.put(machineAreaInfo.getMachineName(), machineAreaInfo.getArea());
            areaCode.add(machineAreaInfo.getArea());
        }
        areaConfig.setAreaInfo(areaInfo).setAreaCode(areaCode);
        return areaConfig;
    }
}