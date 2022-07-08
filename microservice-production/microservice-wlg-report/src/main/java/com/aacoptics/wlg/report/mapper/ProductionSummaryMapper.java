package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.ProductionSummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductionSummaryMapper extends BaseMapper<ProductionSummary> {

    List<String> findProductionDateByMonth(@Param("projectName") String projectName,
                                     @Param("productionDateStart") LocalDate productionDateStart,
                                     @Param("productionDateEnd") LocalDate productionDateEnd);

    List<Map<String, Object>> queryProductionSummaryByCondition(@Param("selectColumn") String selectColumn,
                                                                @Param("pivotIn") String pivotIn,
                                                                @Param("projectName") String projectName,
                                                                @Param("productionDateStart") LocalDate productionDateStart,
                                                                @Param("productionDateEnd") LocalDate productionDateEnd);

}
