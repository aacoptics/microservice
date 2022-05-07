package com.aacoptics.organization.service.impl;

import com.aacoptics.organization.entity.po.RoleMenu;
import com.aacoptics.organization.mapper.RoleMenuMapper;
import com.aacoptics.organization.service.IRoleMenuService;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Override
    @Transactional
    public boolean saveBatch(Long roleId, Set<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds))
            return false;
        removeByRoleId(roleId);
        Set<RoleMenu> userRoles = menuIds.stream().map(menuId -> new RoleMenu(roleId, menuId)).collect(Collectors.toSet());
        return saveBatch(userRoles);
    }

    @Override
    @Transactional
    public boolean removeByRoleId(Long roleId) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenu::getRoleId, roleId);
        return remove(queryWrapper);
    }

    @Override
    @Cached(area = "shortTime", name = "menu4role::", key = "#roleId", cacheType = CacheType.REMOTE)
    public Set<Long> queryByRoleId(Long roleId) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<RoleMenu> userRoleList = list(queryWrapper);
        return userRoleList.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
    }

    @Override
    public List<RoleMenu> queryByRoleIds(Set<Long> roleIds) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        return this.list(queryWrapper);
    }
}
