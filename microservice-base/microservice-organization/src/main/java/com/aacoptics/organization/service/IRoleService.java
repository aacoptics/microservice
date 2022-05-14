package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.RoleQueryParam;
import com.aacoptics.organization.entity.po.Role;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface IRoleService {

    /**
     * 新增角色
     */
    boolean add(Role role);

    /**
     * 更新角色信息
     */
    boolean update(Role role);

    /**
     * 根据id删除角色
     */
    boolean delete(Long id);

    /**
     * 获取角色
     */
    Role get(Long id);

    /**
     * 获取所有角色
     */
    List<Role> listAll();

    /**
     * 查询角色
     */
    @SuppressWarnings("rawtypes")
    IPage<Role> query(Page page, RoleQueryParam roleQueryParam);

    /**
     * 根据用户id查询用户拥有的角色
     */
    List<Role> listByUserId(Long userId);

    /**
     * 根据code获取所有角色
     */
    List<Role> listByCode(String code);

}
