package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.ResourceQueryParam;
import com.aacoptics.organization.entity.po.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface IResourceService {
    /**
     * 获取资源
     *
     * @param id
     * @return
     */
    Resource get(Long id);

    Resource getByCode(String code);

    /**
     * 新增资源
     *
     * @param resource
     * @return
     */
    boolean add(Resource resource);

    /**
     * 查询资源,分页
     *
     * @return
     */
    IPage<Resource> query(Page page, ResourceQueryParam resourceQueryParam);

    /**
     * 查询所有资源
     *
     * @return
     */
    List<Resource> getAll();

    /**
     * 查询所有需要鉴权的资源
     *
     * @return
     */
    List<Resource> getAllAuthentication();

    /**
     * 根据username查询角色拥有的资源
     *
     * @return
     */
    List<Resource> query(String username);

    /**
     * 根据roleId查询角色拥有的资源
     *
     * @return
     */
    List<Resource> query(Long roleId);

    /**
     * 更新资源信息
     *
     * @param resource
     */
    boolean update(Resource resource);

    /**
     * 根据id删除资源
     *
     * @param id
     */
    boolean delete(Long id);
}
