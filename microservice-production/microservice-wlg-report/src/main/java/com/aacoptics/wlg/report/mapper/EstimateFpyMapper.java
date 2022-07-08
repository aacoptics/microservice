package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.EstimateFpy;
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
public interface EstimateFpyMapper extends BaseMapper<EstimateFpy> {


    IPage<Map<String, Object>> queryEstimateFpyByCondition(@Param("page") Page page,
                                                           @Param("projectName") String projectName,
                                                           @Param("mold") String mold,
                                                           @Param("cycle") String cycle,
                                                           @Param("fpyDateStart") LocalDate fpyDateStart,
                                                           @Param("fpyDateEnd") LocalDate fpyDateEnd);

}
