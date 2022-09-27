package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.UmsContent;
import com.aacoptics.notification.mapper.UmsContentMapper;
import com.aacoptics.notification.service.UmsContentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UmsContentServiceImpl extends ServiceImpl<UmsContentMapper, UmsContent> implements UmsContentService {

    @Override
    public List<UmsContent> getUmsContent(String conType) {
        QueryWrapper<UmsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_status", "0")
        .eq("con_type", conType);
        return this.list(queryWrapper);
    }

    @Override
    public List<UmsContent> getUmsContentByBatchId(String conType, String batchId) {
        QueryWrapper<UmsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("con_type", conType);
        queryWrapper.eq("batch_id", batchId);
        return this.list(queryWrapper);
    }


}
