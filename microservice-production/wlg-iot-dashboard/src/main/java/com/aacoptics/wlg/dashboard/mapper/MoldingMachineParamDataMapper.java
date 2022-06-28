package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
}