package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.param.DictDataQueryParam;
import com.aacoptics.organization.entity.po.DictData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 17:14
 */
public interface IDictDataService extends IService<DictData> {
    List<DictData> listByType(String dictType);

    IPage<DictData> listByAll(Page<DictData> page, DictDataQueryParam dictDataQueryParam);

    boolean add(DictData dictData);

    boolean update(DictData dictData);

    boolean delete(Long id);
}

