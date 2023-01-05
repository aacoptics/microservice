package com.aacoptics.temphumidity.mapper;

import com.aacoptics.temphumidity.entity.ElectricMeterInfo;
import com.aacoptics.temphumidity.entity.ElectricMeterPowerQty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ElectricMeterMapper extends BaseMapper<ElectricMeterInfo> {
    List<ElectricMeterInfo> getElectricMeterInfo(@Param("buildingNo") String buildingNo, @Param("floorNo") String floorNo, @Param("roomNo") String roomNo, @Param("meterNo") String meterNo, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<String> getElectricMeterBuildingNoInfo();

    List<String> getElectricMeterFloorNoInfo();

    List<String> getElectricMeterRoomNoInfo();

    List<String> getElectricMeterNoInfo();

    List<ElectricMeterPowerQty> getPowerQty(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
