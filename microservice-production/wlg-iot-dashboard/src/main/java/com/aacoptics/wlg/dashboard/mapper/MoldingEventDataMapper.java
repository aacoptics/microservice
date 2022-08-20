package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.MoldingEventData;
import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface MoldingEventDataMapper extends BaseMapper<MoldingEventData> {

    List<MoldingEventData> getMachineEvents(@Param("machineName") String machineName,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);
}