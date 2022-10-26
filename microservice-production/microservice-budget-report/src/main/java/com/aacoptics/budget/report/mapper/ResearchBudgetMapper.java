package com.aacoptics.budget.report.mapper;

import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ResearchBudgetMapper extends BaseMapper<ResearchBudget> {

    List<Integer> findResearchBudgetAllYearByUploadLogId(@Param("uploadLogId") Long uploadLogId);

    List<Integer> findResearchBudgetAllYearByCondition(@Param("businessDivision") String businessDivision,
                                                       @Param("productLineList") List<String> productLineList);


    ResearchBudget findByBusinessDivisionAndProductLine(@Param("businessDivision") String businessDivision, @Param("productLine") String productLine);


    List<Map<String, Object>> findResearchBudgetByUploadLogId(@Param("selectColumn") String selectColumn,
                                                              @Param("uploadLogId") Long uploadLogId,
                                                              @Param("firstYear") Integer firstYear,
                                                              @Param("secondYear") Integer secondYear);

    List<Map<String, Object>> findResearchBudgetByCondition(@Param("selectColumn") String selectColumn,
                                                              @Param("businessDivision") String businessDivision,
                                                              @Param("productLineList") List<String> productLineList,
                                                              @Param("firstYear") Integer firstYear,
                                                              @Param("secondYear") Integer secondYear);

}
