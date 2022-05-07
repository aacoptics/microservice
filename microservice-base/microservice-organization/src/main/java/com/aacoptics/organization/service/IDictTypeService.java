package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.DictTypeQueryParam;
import com.aacoptics.organization.entity.po.DictType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 10:20
 */
public interface IDictTypeService extends IService<DictType> {

    IPage<DictType> listByAll(Page<DictType> page, DictTypeQueryParam dictTypeQueryParam);

    DictType getByPrimaryKey(Long id);

    boolean add(DictType dictType);

    boolean update(DictType dictType);

    boolean delete(Long id);
}
