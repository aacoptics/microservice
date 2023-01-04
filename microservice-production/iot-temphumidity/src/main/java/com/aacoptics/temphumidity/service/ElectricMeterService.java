package com.aacoptics.temphumidity.service;

import com.aacoptics.temphumidity.entity.ElectricMeterInfo;
import com.aacoptics.temphumidity.entity.ElectricMeterPowerQty;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ElectricMeterService extends IService<ElectricMeterInfo> {
    List<ElectricMeterInfo> getElectricMeterInfo(String buildingNo, String floorNo, String roomNo, String meterNo, String startDate, String endDate);

    List<String> getElectricMeterQueryDataList(String type);

    List<ElectricMeterPowerQty> getPowerQty(String startDate, String endDate);
}
