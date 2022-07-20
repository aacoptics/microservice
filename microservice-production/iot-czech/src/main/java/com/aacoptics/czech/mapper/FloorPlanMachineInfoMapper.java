package com.aacoptics.czech.mapper;

import com.aacoptics.czech.entity.FloorPlanMachineInfo;
import com.aacoptics.czech.entity.StatusInfo;
import com.aacoptics.czech.entity.TemperaturePlotInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FloorPlanMachineInfoMapper extends BaseMapper<FloorPlanMachineInfo> {

    List<FloorPlanMachineInfo> getMachineInfoByFloor(@Param("startNumber") int startNumber, @Param("endNumber") int endNumber);

    List<FloorPlanMachineInfo> getAllMachineInfo();

    float getTemperatureByMachineNo(@Param("machineNumber") String machineNumber);

    FloorPlanMachineInfo getMachineInfoByMachineNumber(@Param("machineNumber") int machineNumber);

    List<TemperaturePlotInfo> getSpindleTemperature(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("machineNumber") String machineNumber);

    List<TemperaturePlotInfo> getAirTemperature(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("machineNumber") String machineNumber);

    List<TemperaturePlotInfo> getBearingTemperature(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("machineNumber") String machineNumber);

    List<TemperaturePlotInfo> getMotorTemperature(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("machineNumber") String machineNumber);

    List<StatusInfo> getStatusInfoByMachineNumber(@Param("machineNumber") int machineNumber);
}
