package com.aacoptics.wlg.report.mapper;

import com.aacoptics.wlg.report.entity.po.CustomerRequirement;
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
public interface CustomerRequirementMapper extends BaseMapper<CustomerRequirement> {


    IPage<Map<String, Object>> queryCustomerRequirementByCondition(@Param("page") Page page,
                                                           @Param("projectName") String projectName,
                                                           @Param("requirementDateStart") LocalDate fpyDateStart,
                                                           @Param("requirementDateEnd") LocalDate fpyDateEnd);

}
