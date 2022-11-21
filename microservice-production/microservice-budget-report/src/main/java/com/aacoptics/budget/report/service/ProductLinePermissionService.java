package com.aacoptics.budget.report.service;


import com.aacoptics.budget.report.entity.param.ProductLinePermissionQueryParam;
import com.aacoptics.budget.report.entity.po.ProductLinePermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ProductLinePermissionService {


    /**
     * 获取所有产品线权限配置
     *
     * @return
     */
    List<ProductLinePermission> getAll();


    /**
     * 根据条件查询产品线权限配置
     *
     * @return
     */
    IPage<ProductLinePermission> query(Page page, ProductLinePermissionQueryParam productLinePermissionQueryParam);


    /**
     * 更新产品线权限信息
     *
     * @param productLinePermission
     */
    boolean update(ProductLinePermission productLinePermission);

    /**
     * 根据id删除产品线权限
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增产品线权限
     *
     * @param productLinePermission
     * @return
     */
    boolean add(ProductLinePermission productLinePermission);



    /**
     * 获取产品线权限
     *
     * @param id
     * @return
     */
    ProductLinePermission get(Long id);

    /**
     * 通过用户域账号获取产品线权限
     *
     * @param userCode
     * @return
     */
    List<ProductLinePermission> getByUserCode(String userCode);
}
