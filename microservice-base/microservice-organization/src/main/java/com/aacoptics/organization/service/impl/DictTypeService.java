package com.aacoptics.organization.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aacoptics.organization.entity.param.DictTypeQueryParam;
import com.aacoptics.organization.entity.po.DictData;
import com.aacoptics.organization.entity.po.DictType;
import com.aacoptics.organization.exception.BusinessException;
import com.aacoptics.organization.exception.DataNotFoundException;
import com.aacoptics.organization.exception.OrganizationErrorType;
import com.aacoptics.organization.mapper.DictTypeMapper;
import com.aacoptics.organization.service.IDictDataService;
import com.aacoptics.organization.service.IDictTypeService;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 10:20
 */
@Service
@Slf4j
public class DictTypeService extends ServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {

    @Resource
    private IDictDataService dictDataService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public IPage<DictType> listByAll(Page<DictType> page, DictTypeQueryParam dictTypeQueryParam) {
        LambdaQueryWrapper<DictType> queryWrapper = dictTypeQueryParam.build().lambda();
        queryWrapper.like(StringUtils.isNotBlank(dictTypeQueryParam.getDictName()),
                DictType::getDictName, dictTypeQueryParam.getDictName());
        queryWrapper.like(StringUtils.isNotBlank(dictTypeQueryParam.getDictType()),
                DictType::getDictType, dictTypeQueryParam.getDictType());
        queryWrapper.eq(StringUtils.isNotBlank(dictTypeQueryParam.getStatus()),
                DictType::getStatus, dictTypeQueryParam.getStatus());

        queryWrapper.orderByAsc(DictType::getDictName);
        queryWrapper.orderByAsc(DictType::getDictType);

        return this.page(page, queryWrapper);
    }

    @Override
    public DictType getByPrimaryKey(Long id) {
        DictType dictType = this.getById(id);
        if (Objects.isNull(dictType)) {
            throw new DataNotFoundException("data not found with id:" + id);
        }
        return dictType;
    }

    @Override
    @Transactional
    public boolean add(DictType dictType) {
        List<DictType> list = this.list(new QueryWrapper<DictType>().lambda()
                .eq(DictType::getDictType, dictType.getDictType())
        );

        if (list.size() > 0) throw new BusinessException(OrganizationErrorType.UNIQUE_Exception);

        return this.save(dictType);
    }

    @Override
    @Transactional
    @CacheInvalidate(name = "dictData4DictType::", key = "#dictType.dictType")
    public boolean update(DictType dictType) {
        DictType oldDict = this.getByPrimaryKey(dictType.getId());
        dictDataService.update(new LambdaUpdateWrapper<DictData>().eq(DictData::getDictType, oldDict.getDictType())
                .set(DictData::getDictType, dictType.getDictType()));

        return this.updateById(dictType);
    }

    @Override
    @Transactional
    @CacheInvalidate(name = "dictData4DictType::", key = "targetObject.getDictData4DictTypeKeys()", multi = true)
    public boolean delete(Long id) {
        DictType dictType = this.getByPrimaryKey(id);
        if (dictDataService.list(new LambdaQueryWrapper<DictData>().eq(DictData::getDictType, dictType.getDictType())).size() > 0)
            throw new BusinessException(OrganizationErrorType.DELETE_Exception, String.format("%1$s已分配,不能删除", dictType.getDictName()));

        return this.removeById(id);
    }

    public Set<String> getDictData4DictTypeKeys() {
        Set<String> dictData4DictTypeKeys = stringRedisTemplate.keys("dictData4DictType::*");
        if (CollUtil.isEmpty(dictData4DictTypeKeys)) return Collections.singleton("dictData4DictType::*");
        return dictData4DictTypeKeys.stream().map(key -> key.replace("dictData4DictType::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

}
