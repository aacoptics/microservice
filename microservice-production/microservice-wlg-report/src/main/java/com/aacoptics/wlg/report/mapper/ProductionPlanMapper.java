package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.ProductionPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ProductionPlanMapper extends BaseMapper<ProductionPlan> {

    List<String> findPlanDateByMonth(@Param("projectName") String projectName,
                                     @Param("mold") String mold,
                                     @Param("cycle") String cycle,
                                     @Param("planDateStart") LocalDate planDateStart,
                                     @Param("planDateEnd") LocalDate planDateEnd);


    List<Map<String, Object>> findProductionPlanByMonth(@Param("projectName") String projectName,
                                                        @Param("mold") String mold,
                                                        @Param("cycle") String cycle,
                                                        @Param("selectColumn") String selectColumn,
                                                        @Param("pivotIn") String pivotIn,
                                                        @Param("selectVarcharColumn") String selectVarcharColumn,
                                                        @Param("selectJHCHANCHUColumn") String selectJHCHANCHUColumn,
                                                        @Param("selectJHLINGLIAOColumn") String selectJHLINGLIAOColumn,
                                                        @Param("selectJHHDCHANCHUColumn") String selectJHHDCHANCHUColumn,
                                                        @Param("selectJHZHITONGLVColumn") String selectJHZHITONGLVColumn,
                                                        @Param("selectJHHDCHANCHUSumColumn") String selectJHHDCHANCHUSumColumn,
                                                        @Param("selectJHCHANCHUSumColumn") String selectJHCHANCHUSumColumn,
                                                        @Param("selectJHLINGLIAOSumColumn") String selectJHLINGLIAOSumColumn,
                                                        @Param("planDateStart") LocalDate planDateStart,
                                                        @Param("planDateEnd") LocalDate planDateEnd);


}
