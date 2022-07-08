package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.ProductionActual;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

@Repository
@Mapper
public interface ProductionActualMapper extends BaseMapper<ProductionActual> {


    IPage<Map<String, Object>> queryProductionActualByCondition(@Param("page") Page page,
                                                                @Param("projectName") String projectName,
                                                                @Param("product") String product,
                                                                @Param("mold") String mold,
                                                                @Param("cycle") String cycle,
                                                                @Param("actualDateStart") LocalDate actualDateStart,
                                                                @Param("actualDateEnd") LocalDate actualDateEnd);

}
