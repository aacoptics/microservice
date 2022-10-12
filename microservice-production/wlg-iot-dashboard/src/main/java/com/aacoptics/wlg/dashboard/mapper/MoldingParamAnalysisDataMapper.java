package com.aacoptics.wlg.dashboard.mapper;


import com.aacoptics.wlg.dashboard.entity.InputReport;
import com.aacoptics.wlg.dashboard.entity.MoldingParamAnalysisData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface MoldingParamAnalysisDataMapper extends BaseMapper<MoldingParamAnalysisData> {
    List<MoldingParamAnalysisData> getAnalysisData(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime,
                                              @Param("machineName") String machineName,
                                              @Param("paramNames")  List<String> paramNames);

}