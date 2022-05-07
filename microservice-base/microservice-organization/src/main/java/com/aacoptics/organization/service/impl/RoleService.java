package com.aacoptics.organization.service.impl;

import com.aacoptics.organization.entity.param.RoleQueryParam;
import com.aacoptics.organization.entity.po.Role;
import com.aacoptics.organization.exception.RoleNotFoundException;
import com.aacoptics.organization.mapper.RoleMapper;
import com.aacoptics.organization.service.IRoleMenuService;
import com.aacoptics.organization.service.IRoleResourceService;
import com.aacoptics.organization.service.IRoleService;
import com.aacoptics.organization.service.IUserRoleService;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleResourceService roleResourceService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean add(Role role) {
        boolean isSuccess = this.save(role);
        roleResourceService.saveBatch(role.getId(), role.getResourceIds());
        roleMenuService.saveBatch(role.getId(), role.getMenuIds());
        return isSuccess;
    }

    @Override
    @CacheInvalidate(name = "role::", key = "#id")
    public boolean delete(Long id) {
        roleResourceService.removeByRoleId(id);
        roleMenuService.removeByRoleId(id);
        return this.removeById(id);
    }

    @Override
    @CacheInvalidate(name = "role::", key = "#role.id")
    @CacheInvalidate(name = "menu4user::", key = "targetObject.getMenu4UserKeys()", multi = true)
    @CacheInvalidate(name = "resource4user::", key = "targetObject.getResource4UserKeys()", multi = true)
    @CacheInvalidate(area = "shortTime", name = "resource4role::", key = "#role.id")
    public boolean update(Role role) {
        boolean isSuccess = this.updateById(role);
        roleResourceService.saveBatch(role.getId(), role.getResourceIds());
        roleMenuService.saveBatch(role.getId(), role.getMenuIds());
        return isSuccess;
    }

    public Set<String> getMenu4UserKeys(){
        Set<String> menu4UserKeys = stringRedisTemplate.keys("menu4user::" + "*");
        return menu4UserKeys.stream().map(key -> {
            return key.replace("menu4user::", StringUtils.EMPTY);
        }).collect(Collectors.toSet());
    }

    public Set<String> getResource4UserKeys(){
        Set<String> resource4UserKeys = stringRedisTemplate.keys("resource4user::" + "*");
        return resource4UserKeys.stream().map(key -> {
            return key.replace("resource4user::", StringUtils.EMPTY);
        }).collect(Collectors.toSet());
    }

    @Override
    @Cached(name = "role::", key = "#id", cacheType = CacheType.REMOTE)
    public Role get(Long id) {
        Role role = this.getById(id);
        if (Objects.isNull(role)) {
            throw new RoleNotFoundException("role not found with id:" + id);
        }
        role.setResourceIds(roleResourceService.queryByRoleId(id));
        return role;
    }

    @Override
    public List<Role> getAll() {
        return this.list();
    }

    @Override
    @Cached(name = "role4user::", key = "#userId", cacheType = CacheType.REMOTE)
    public List<Role> query(Long userId) {
        Set<Long> roleIds = userRoleService.queryByUserId(userId);
        return (List<Role>) this.listByIds(roleIds);
    }

    @Override
    public IPage<Role> query(Page page, RoleQueryParam roleQueryParam) {
        QueryWrapper<Role> queryWrapper = roleQueryParam.build();
        queryWrapper.eq(StringUtils.isNotBlank(roleQueryParam.getName()), "name", roleQueryParam.getName());
        queryWrapper.eq(StringUtils.isNotBlank(roleQueryParam.getCode()), "code", roleQueryParam.getCode());
        return this.page(page, queryWrapper);
    }
}
