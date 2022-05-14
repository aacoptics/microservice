package com.aacoptics.organization.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aacoptics.organization.entity.param.DictDataQueryParam;
import com.aacoptics.organization.entity.po.DictData;
import com.aacoptics.organization.mapper.DictDataMapper;
import com.aacoptics.organization.service.IDictDataService;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 17:18
 */
@Service
@Slf4j
public class DictDataService extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Cached(name = "dictData4DictType::", key = "#dictType", cacheType = CacheType.REMOTE)
    public List<DictData> listByType(String dictType) {
        return this.list(new LambdaQueryWrapper<DictData>().eq(DictData::getDictType, dictType).eq(DictData::getStatus, '0').orderByAsc(DictData::getDictSort));
    }

    @Override
    public IPage<DictData> listByAll(Page<DictData> page, DictDataQueryParam dictDataQueryParam) {
        LambdaQueryWrapper<DictData> queryWrapper = dictDataQueryParam.build().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getDictType()), DictData::getDictType, dictDataQueryParam.getDictType());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getDictLabel()), DictData::getDictLabel, dictDataQueryParam.getDictLabel());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getDictValue()), DictData::getDictValue, dictDataQueryParam.getDictValue());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataQueryParam.getStatus()), DictData::getStatus, dictDataQueryParam.getStatus());

        queryWrapper.orderByAsc(DictData::getDictType);
        queryWrapper.orderByAsc(DictData::getDictSort);

        return this.page(page, queryWrapper);
    }

    @Override
    @CacheInvalidate(name = "dictData4DictType::", key = "#dictData.dictType")
    public boolean add(DictData dictData) {
        return this.save(dictData);
    }

    @Override
    @CacheInvalidate(name = "dictData4DictType::", key = "#dictData.dictType")
    public boolean update(DictData dictData) {
        return this.updateById(dictData);
    }

    @Override
    @CacheInvalidate(name = "dictData4DictType::", key = "targetObject.getDictData4DictTypeKeys()", multi = true)
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    public Set<String> getDictData4DictTypeKeys() {
        Set<String> dictData4DictTypeKeys = stringRedisTemplate.keys("dictData4DictType::*");
        if (CollUtil.isEmpty(dictData4DictTypeKeys)) return Collections.singleton("dictData4DictType::*");
        return dictData4DictTypeKeys.stream().map(key -> key.replace("dictData4DictType::", StringUtils.EMPTY)).collect(Collectors.toSet());
    }

}
