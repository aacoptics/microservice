package com.aacoptics.temphumidity.mapper;

import com.aacoptics.temphumidity.entity.ElectricData;
import com.aacoptics.temphumidity.entity.SmartMeterInfo;
import com.aacoptics.temphumidity.entity.TempHumidityData;
import com.aacoptics.temphumidity.entity.TemphumidityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TemphumidityMapper extends BaseMapper<TemphumidityInfo> {


    List<TemphumidityInfo> getTemphumidityInfoByDate(@Param("date") String date);

    List<SmartMeterInfo> getSmartMeterInfoByDate(@Param("date") String date);

    List<SmartMeterInfo> getRawEletricData();

    List<ElectricData> getTop1ElectricData(@Param("deviceId") int deviceId);

    List<SmartMeterInfo> getLastRawEletricData(Map<String, Object> param);

    String getDeviceNameById(@Param("deviceId") int deviceId);

    void insertElectricData(Map<String, Object> param);

    List<TemphumidityInfo> getRawTempHumidityData();

    void insertTempHumidityData(Map<String, Object> param);

    List<Map<String, Object>> getTemphumidityInfoDisplay(Map<String, Object> param);

}
