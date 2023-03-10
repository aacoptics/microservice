package com.aacoptics.temphumidity.service;

import com.aacoptics.temphumidity.entity.SmartMeterInfo;
import com.aacoptics.temphumidity.entity.TemphumidityInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface TemphumidityService extends IService<TemphumidityInfo> {
    /**
     * 根据日期获取温湿度信息
     * @param date 日期
     * @return 该日期所有温湿度信息
     */
    List<TemphumidityInfo> getTemphumidityInfoByDate(String date);

    List<SmartMeterInfo> getSmartMeterInfoByDate(String date);

    void updateElectricData();

    void updateTempHumidityData();

    List<Map<String, Object>> getTemphumidityInfoDisplay(String buildingNo, String floorNo, String startDate, String endDate);

}
