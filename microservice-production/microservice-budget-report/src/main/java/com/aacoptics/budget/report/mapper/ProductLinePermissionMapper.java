package com.aacoptics.budget.report.mapper;

import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface ProductLinePermissionMapper extends BaseMapper<ProductLinePermission> {

}