package com.aacoptics.organization.mapper;

import com.aacoptics.organization.entity.po.MenuAccessLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MenuAccessLogMapper extends BaseMapper<MenuAccessLog> {
}