package com.aacoptics.temphumidity.service.impl;

import com.aacoptics.temphumidity.entity.TemphumidityInfo;
import com.aacoptics.temphumidity.mapper.TemphumidityMapper;
import com.aacoptics.temphumidity.service.TemphumidityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TemphumidityServiceImpl extends ServiceImpl<TemphumidityMapper, TemphumidityInfo> implements TemphumidityService {

    @Autowired
    TemphumidityMapper temphumidityMapper;

    @Override
    public List<TemphumidityInfo> getTemphumidityInfoByDate(String date) {
        return temphumidityMapper.getTemphumidityInfoByDate(date);
    }
}
