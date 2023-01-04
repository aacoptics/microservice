package com.aacoptics.temphumidity.service.impl;

import com.aacoptics.temphumidity.entity.ElectricMeterInfo;
import com.aacoptics.temphumidity.entity.ElectricMeterPowerQty;
import com.aacoptics.temphumidity.mapper.ElectricMeterMapper;
import com.aacoptics.temphumidity.service.ElectricMeterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ElectricMeterServiceImpl extends ServiceImpl<ElectricMeterMapper, ElectricMeterInfo> implements ElectricMeterService {
    @Autowired
    ElectricMeterMapper electricMeterMapper;

    @Override
    public List<ElectricMeterInfo> getElectricMeterInfo(String buildingNo, String floorNo, String roomNo, String meterNo, String startDate, String endDate) {
        return electricMeterMapper.getElectricMeterInfo(buildingNo, floorNo, roomNo, meterNo, startDate, endDate);
    }

    @Override
    public List<String> getElectricMeterQueryDataList(String type) {
        List<String> result = new ArrayList<>();
        if("buildingNo".equals(type)) {
            result = electricMeterMapper.getElectricMeterBuildingNoInfo();
        } else if("floorNo".equals(type)) {
            result = electricMeterMapper.getElectricMeterFloorNoInfo();
        } else if("roomNo".equals(type)) {
            result = electricMeterMapper.getElectricMeterRoomNoInfo();
        } else if("meterNo".equals(type)) {
            result = electricMeterMapper.getElectricMeterNoInfo();
        }
        return result;
    }

    @Override
    public List<ElectricMeterPowerQty> getPowerQty(String startDate, String endDate) {
        return electricMeterMapper.getPowerQty(startDate, endDate);
    }
}
