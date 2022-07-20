package com.aacoptics.lenspacker.dashboard.service.impl;

import com.aacoptics.lenspacker.dashboard.dao.LensInfoMapper;
import com.aacoptics.lenspacker.dashboard.entity.LensInfo;
import com.aacoptics.lenspacker.dashboard.service.LensInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LensInfoServiceImpl extends ServiceImpl<LensInfoMapper, LensInfo> implements LensInfoService {

    @Autowired
    LensInfoMapper lensInfoMapper;

    @Override
    public List<LensInfo> getLensPackerMachineInfo(){
        return lensInfoMapper.getLensPackerMachineInfo();
    }
}
