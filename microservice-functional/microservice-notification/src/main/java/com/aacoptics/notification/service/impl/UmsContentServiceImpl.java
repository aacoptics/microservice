package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.UmsContent;
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
    public List<UmsContent> getUmsContent() {
        QueryWrapper<UmsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_status", "0");
        return this.list(queryWrapper);
    }


}
