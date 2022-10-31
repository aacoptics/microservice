package com.aacoptics.budget.report.mapper;

import com.aacoptics.budget.report.entity.po.BusinessDivisionProductLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface BusinessDivisionProductLineMapper extends BaseMapper<BusinessDivisionProductLine> {

    List<String> getAllBusinessDivision();

    List<String> getProductLineByBusinessDivision(@Param("businessDivision") String businessDivision,
                                                  @Param("verificationPermission") boolean verificationPermission,
                                                  @Param("userCode") String userCode);


}