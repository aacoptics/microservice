package com.aacoptics.organization.service.impl;

import com.aacoptics.organization.entity.param.DictDataQueryParam;
import com.aacoptics.organization.entity.po.DictData;
import com.aacoptics.organization.mapper.DictDataMapper;
import com.aacoptics.organization.service.IDictDataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 17:18
 */
@Service
@Slf4j
public class DictDataService extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    @Override
    public List<DictData> listByType(String dictType) {
        return this.list(new LambdaQueryWrapper<DictData>()
                .eq(DictData::getDictType, dictType)
                .eq(DictData::getStatus, '0')
                .orderByAsc(DictData::getDictSort));
    }

    @Override
    public IPage<DictData> listByAll(Page<DictData> page, DictDataQueryParam dictDataQueryParam) {
        LambdaQueryWrapper<DictData> queryWrapper = dictDataQueryParam.build().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getDictType()),
                DictData::getDictType, dictDataQueryParam.getDictType());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getDictLabel()),
                DictData::getDictLabel, dictDataQueryParam.getDictLabel());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getDictValue()),
                DictData::getDictValue, dictDataQueryParam.getDictValue());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getStatus()),
                DictData::getStatus, dictDataQueryParam.getStatus());

        queryWrapper.orderByAsc(DictData::getDictType);
        queryWrapper.orderByAsc(DictData::getDictSort);

        return this.page(page, queryWrapper);
    }

    @Override
    public boolean add(DictData dictData) {
        return this.save(dictData);
    }

    @Override
    public boolean update(DictData dictData) {
        return this.updateById(dictData);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

}
