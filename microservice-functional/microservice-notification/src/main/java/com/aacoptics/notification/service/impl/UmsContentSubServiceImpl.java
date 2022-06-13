package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.UmsContentSub;
import com.aacoptics.notification.mapper.UmsContentSubMapper;
import com.aacoptics.notification.service.UmsContentSubService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UmsContentSubServiceImpl extends ServiceImpl<UmsContentSubMapper, UmsContentSub> implements UmsContentSubService {

    @Override
    public List<UmsContentSub> getUmsContentSub(String batchId) {
        QueryWrapper<UmsContentSub> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_id", batchId)
        .orderByAsc("seq_no");
        return this.list(queryWrapper);
    }

}
