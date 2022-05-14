package com.aacoptics.organization.service;

import cn.hutool.core.lang.tree.Tree;
import com.aacoptics.organization.entity.po.Menu;

import java.util.List;

public interface IMenuService {

    /**
     * 新增菜单
     */
    boolean add(Menu menu);

    /**
     * 根据父id查询菜单
     */
    List<Menu> listByParentId(Long id);

    /**
     * 更新菜单信息
     */
    boolean update(Menu menu);

    /**
     * 根据id删除菜单
     */
    boolean delete(Long id);

    /**
     * 获取菜单
     */
    Menu get(Long id);

    /**
     * 查询所有菜单
     */
    List<Tree<String>> listAll();

    /**
     * 根据菜单名称获取所有菜单
     */
    List<Tree<String>> listByName(String name);

    /**
     * 根据用户名获取所有菜单
     */
    List<Tree<String>> listByUsername(String username);

    /**
     * 根据角色Id获取所有菜单
     */
    List<Tree<String>> listByRoleId(Long roleId);
}
