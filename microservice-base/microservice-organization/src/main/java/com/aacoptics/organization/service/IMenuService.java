package com.aacoptics.organization.service;

import cn.hutool.core.lang.tree.Tree;
import com.aacoptics.organization.entity.po.Menu;

import java.util.List;

public interface IMenuService {
    /**
     * 获取菜单
     *
     * @param id
     * @return
     */
    Menu get(Long id);

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    boolean add(Menu menu);
//
//    /**
//     * 查询菜单
//     *
//     * @return
//     */
//    List<Menu> query(MenuQueryParam menuQueryParam);
//
    /**
     * 根据父id查询菜单
     *
     * @return
     */
    List<Menu> queryByParentId(Long id);

    /**
     * 更新菜单信息
     *
     * @param menu
     */
    boolean update(Menu menu);

    /**
     * 根据id删除菜单
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 查询所有菜单
     *
     * @return
     */
    List<Tree<String>> getAll();

    List<Tree<String>> getByName(String name);

    List<Tree<String>> getByUsername(String username);

    List<Tree<String>> getByRoleId(Long roleId);
}
