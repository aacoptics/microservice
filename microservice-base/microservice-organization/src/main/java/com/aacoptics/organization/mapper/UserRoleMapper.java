package com.aacoptics.organization.mapper;

import com.aacoptics.organization.entity.po.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}