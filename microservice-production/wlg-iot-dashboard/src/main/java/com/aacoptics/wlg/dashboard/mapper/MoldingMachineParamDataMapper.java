package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.po.MoldingMachineParamData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MoldingMachineParamDataMapper extends BaseMapper<MoldingMachineParamData> {
    List<MoldingMachineParamData> getMoldingParamData(@Param("machineName") String machineName,
                                                      @Param("paramName") String paramName,
                                                      @Param("waferIds") List<String> waferIds);

    List<MoldingMachineParamData> getMachineName();

    List<MoldingMachineParamData> getWaferIds(@Param("machineName") String machineName,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

    List<MoldingMachineParamData> getMoldingParamName(@Param("machineName") String machineName,
                                                      @Param("waferIds") List<String> waferIds);

    List<Map<String, Object>> getMoldingStatusData(@Param("machineName") List<String> machineName, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Map<String, Object>> getMoldingMk4ExportData(@Param("machineName") List<String> machineName, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Map<String, Object>> getMoldingSingleStatusData(@Param("machineName") List<String> machineName, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}