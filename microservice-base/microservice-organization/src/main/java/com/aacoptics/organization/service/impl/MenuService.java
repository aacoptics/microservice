package com.aacoptics.organization.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.aacoptics.organization.entity.po.Menu;
import com.aacoptics.organization.exception.ExistChildMenuException;
import com.aacoptics.organization.mapper.MenuMapper;
import com.aacoptics.organization.service.IMenuService;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuService extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public boolean add(Menu menu) {
        return this.save(menu);
    }

    @Override
    @CacheInvalidate(name = "menu::", key = "#id")
    @CacheInvalidate(name = "menu4user::", key = "targetObject.getMenu4UserKeys()", multi = true)
    public boolean delete(Long id) {
        List<Menu> childMenu = this.listByParentId(id);
        if (childMenu != null && childMenu.size() > 0) {
            throw new ExistChildMenuException("请先删除子菜单");
        }

        return this.removeById(id);
    }

    @Override
    @CacheInvalidate(name = "menu::", key = "#menu.id")
    @CacheInvalidate(name = "menu4user::", key = "targetObject.getMenu4UserKeys()", multi = true)
    public boolean update(Menu menu) {
        return this.updateById(menu);
    }

    @Override
    @Cached(name = "menu::", key = "#id", cacheType = CacheType.REMOTE)
    public Menu get(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Menu> listByParentId(Long parentId) {
        return this.list(new QueryWrapper<Menu>().eq("parent_id", parentId));
    }

    @Override
    public List<Tree<String>> listAll() {
        List<Menu> allMenus = menuMapper.getAllMenu();
        return getMenuTrees(allMenus);
    }

    @Override
    public List<Tree<String>> listByName(String name) {
        List<Menu> allMenus = menuMapper.getAllMenu();
        return getMenuTreesByName(getMenuTrees(allMenus), name);
    }

    @Override
    @Cached(name = "menu4user::", key = "#username", cacheType = CacheType.REMOTE)
    public List<Tree<String>> listByUsername(String username) {
        List<Menu> allMenus = menuMapper.getMenuByUsername(username);
        return getMenuTrees(allMenus);
    }

    @Override
    public List<Tree<String>> listByRoleId(Long roleId) {
        List<Menu> allMenus = menuMapper.getMenuByRoleId(roleId);
        return getMenuTrees(allMenus);
    }

    private List<Tree<String>> getMenuTrees(List<Menu> allMenus) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都有默认值的哈
        // 默认支持排序
        treeNodeConfig.setWeightKey("orderNum");
        treeNodeConfig.setParentIdKey("parentId");
        treeNodeConfig.setIdKey("id");
        //转换器
        return TreeUtil.build(allMenus, "-1", treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId().toString());
            tree.setParentId(treeNode.getParentId().toString());
            tree.setWeight(treeNode.getOrderNum());
            tree.setName(treeNode.getName());
            // 扩展属性
            tree.putExtra("path", treeNode.getPath());
            tree.putExtra("component", treeNode.getComponent());
            tree.putExtra("title", treeNode.getTitle());
            tree.putExtra("icon", treeNode.getIcon());
            tree.putExtra("description", treeNode.getDescription());
            tree.putExtra("webUrl", treeNode.getWebUrl());
            tree.putExtra("visible", treeNode.getVisible());
            tree.putExtra("menuType", treeNode.getMenuType());
            tree.putExtra("roles", treeNode.getRoles() == null ? null : Arrays.stream(treeNode.getRoles().split(",")).mapToInt(Integer::parseInt).toArray());
        });
    }

    private List<Tree<String>> getMenuTreesByName(List<Tree<String>> allMenus, String name) {
        List<Tree<String>> menus = new ArrayList<>();
        for (Tree<String> menu : allMenus) {
            if (menu.getName().equals(name)) {
                menus.add(menu);
            }
            if (menu.getChildren() != null && menu.getChildren().size() > 0) {
                menus.addAll(getMenuTreesByName(menu.getChildren(), name));
            }
        }
        return menus;
    }

    public Set<String> getMenu4UserKeys() {
        Set<String> menu4UserKeys = stringRedisTemplate.keys("menu4user::*");
        if (CollUtil.isEmpty(menu4UserKeys)) return Collections.singleton("menu4user::*");
        return menu4UserKeys.stream().map(key -> key.replace("menu4user::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

}
