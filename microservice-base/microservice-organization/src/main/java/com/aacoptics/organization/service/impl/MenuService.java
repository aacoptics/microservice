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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MenuService extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public boolean add(Menu menu) {
        Set<String> keys = redisTemplate.keys("menu4user*");
        if(CollUtil.isNotEmpty(keys)) redisTemplate.delete(keys);
        return this.save(menu);
    }

    @Override
    @CacheInvalidate(name = "menu::", key = "#id")
    public boolean delete(Long id) {
        Set<String> keys = redisTemplate.keys("menu4user*");
        if(CollUtil.isNotEmpty(keys)) redisTemplate.delete(keys);
        List<Menu> childMenu = this.queryByParentId(id);
        if (childMenu != null && childMenu.size() > 0) {
            throw new ExistChildMenuException("请先删除子菜单");
        }

        return this.removeById(id);
    }

    @Override
    @CacheInvalidate(name = "menu::", key = "#menu.id")
    public boolean update(Menu menu) {
        Set<String> keys = redisTemplate.keys("menu4user*");
        if(CollUtil.isNotEmpty(keys)) redisTemplate.delete(keys);
        return this.updateById(menu);
    }

    @Override
    @Cached(name = "menu::", key = "#id", cacheType = CacheType.REMOTE)
    public Menu get(Long id) {
        return this.getById(id);
    }

    //    @Override
//    public List<Menu> query(MenuQueryParam menuQueryParam) {
//        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(null != menuQueryParam.getName(), "name", menuQueryParam.getName());
//        return this.list(queryWrapper);
//    }
//
    @Override
    public List<Menu> queryByParentId(Long id) {
        return this.list(new QueryWrapper<Menu>().eq("parent_id", id));
    }

    @Override
    public List<Tree<String>> getAll() {
        List<Menu> allMenus = menuMapper.getAllMenu();
        return getMenuTrees(allMenus);
    }

    @Override
    public List<Tree<String>> getByName(String name) {
        List<Menu> allMenus = menuMapper.getAllMenu();
        return getMenuTreesByName(getMenuTrees(allMenus), name);
    }

    @Override
    @Cached(name = "menu4user::", key = "#username", cacheType = CacheType.REMOTE)
    public List<Tree<String>> getByUsername(String username) {
        List<Menu> allMenus = menuMapper.getMenuByUsername(username);
        return getMenuTrees(allMenus);
    }

    @Override
    public List<Tree<String>> getByRoleId(Long roleId) {
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
        List<Tree<String>> menus = TreeUtil.build(allMenus, "-1", treeNodeConfig, (treeNode, tree) -> {
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
            tree.putExtra("roles", treeNode.getRoles() == null ? null : Arrays.stream(treeNode.getRoles().split(",")).mapToInt(Integer::parseInt).toArray());
        });
        return menus;
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

}
