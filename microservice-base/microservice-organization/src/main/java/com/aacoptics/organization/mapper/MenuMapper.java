package com.aacoptics.organization.mapper;

import com.aacoptics.organization.entity.po.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getAllMenu();

    List<Menu> getMenuByUsername(@Param("username") String username);

    List<Menu> getMenuByRoleId(@Param("roleId") Long roleId);
}