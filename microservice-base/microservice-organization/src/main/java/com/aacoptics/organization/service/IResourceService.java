package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.ResourceQueryParam;
import com.aacoptics.organization.entity.po.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface IResourceService {

    /**
     * 新增资源
     */
    boolean add(Resource resource);

    /**
     * 更新资源信息
     */
    boolean update(Resource resource);

    /**
     * 根据id删除资源
     */
    boolean delete(Long id);

    /**
     * 获取资源
     */
    Resource get(Long id);

    /**
     * 获取资源
     */
    Resource getByCode(String code);

    /**
     * 查询资源,分页
     */
    @SuppressWarnings("rawtypes")
    IPage<Resource> query(Page page, ResourceQueryParam resourceQueryParam);

    /**
     * 查询所有资源
     */
    List<Resource> listAll();

    /**
     * 查询所有需要鉴权的资源
     */
    List<Resource> listByAuthentication();

    /**
     * 根据username查询角色拥有的资源
     */
    List<Resource> listByUserName(String username);

    /**
     * 根据roleId查询角色拥有的资源
     */
    List<Resource> listByRoleId(Long roleId);

}
