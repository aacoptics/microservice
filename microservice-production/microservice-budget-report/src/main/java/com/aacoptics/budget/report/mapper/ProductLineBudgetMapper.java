package com.aacoptics.budget.report.mapper;

import com.aacoptics.budget.report.entity.po.ProductLineBudget;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductLineBudgetMapper extends BaseMapper<ProductLineBudget> {

    List<Integer> findProductLineBudgetAllYearByUploadLogId(@Param("uploadLogId") Long uploadLogId);

    ProductLineBudget findByBusinessDivisionAndProductLine(@Param("businessDivision") String businessDivision, @Param("productLine") String productLine);


    List<Map<String, Object>> findProductLineBudgetByUploadLogId(@Param("selectColumn") String selectColumn,
                                                              @Param("uploadLogId") Long uploadLogId,
                                                              @Param("firstYear") Integer firstYear,
                                                              @Param("secondYear") Integer secondYear);

}
