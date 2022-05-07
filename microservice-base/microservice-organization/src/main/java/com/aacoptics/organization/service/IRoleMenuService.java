package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.po.RoleMenu;

import java.util.List;
import java.util.Set;

public interface IRoleMenuService {

    /**
     * 批量给角色添加菜单
     *
     * @param roleId      角色id
     * @param menuIds 菜单id列表
     * @return 是否操作成功
     */
    boolean saveBatch(Long roleId, Set<Long> menuIds);

    /**
     * 删除角色拥有的资源
     *
     * @param roleId 角色id
     * @return 是否操作成功
     */
    boolean removeByRoleId(Long roleId);

    /**
     * 查询角色拥有菜单id
     *
     * @param roleId 角色id
     * @return 角色拥有的菜单id集合
     */
    Set<Long> queryByRoleId(Long roleId);

    /**
     * 根据角色id列表查询菜单关系
     *
     * @param roleIds 角色id集合
     * @return 角色菜单关系集合
     */
    List<RoleMenu> queryByRoleIds(Set<Long> roleIds);
}
