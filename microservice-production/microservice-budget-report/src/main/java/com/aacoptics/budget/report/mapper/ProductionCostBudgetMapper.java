package com.aacoptics.budget.report.mapper;

import com.aacoptics.budget.report.entity.po.ProductionCostBudget;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductionCostBudgetMapper extends BaseMapper<ProductionCostBudget> {

    List<Integer> findProductionCostBudgetAllYearByUploadLogId(@Param("uploadLogId") Long uploadLogId);


    List<Integer> findProductionCostBudgetAllYearByCondition(@Param("businessDivision") String businessDivision,
                                                               @Param("productLineList") List<String> productLineList);


    ProductionCostBudget findByBusinessDivisionAndProductLine(@Param("businessDivision") String businessDivision,
                                                        @Param("productLine") String productLine);

    List<Map<String, Object>> findProductionCostBudgetByUploadLogId(@Param("selectColumn") String selectColumn,
                                                              @Param("uploadLogId") Long uploadLogId,
                                                              @Param("firstYear") Integer firstYear,
                                                              @Param("secondYear") Integer secondYear);

    List<Map<String, Object>> findProductionCostBudgetByCondition(@Param("selectColumn") String selectColumn,
                                                                    @Param("businessDivision") String businessDivision,
                                                                    @Param("productLineList") List<String> productLineList,
                                                                    @Param("firstYear") Integer firstYear,
                                                                    @Param("secondYear") Integer secondYear);

}
