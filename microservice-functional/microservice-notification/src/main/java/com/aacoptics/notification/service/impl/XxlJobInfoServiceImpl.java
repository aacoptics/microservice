package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.mapper.XxlJobInfoMapper;
import com.aacoptics.notification.service.XxlJobInfoService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@DS("msg_db")
public class XxlJobInfoServiceImpl extends ServiceImpl<XxlJobInfoMapper, XxlJobInfo> implements XxlJobInfoService {

    @Override
    public boolean add(XxlJobInfo xxlJobInfo) {
        return this.save(xxlJobInfo);
    }

    @Override
    public boolean update(XxlJobInfo xxlJobInfo) {
        return this.updateById(xxlJobInfo);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    public IPage<XxlJobInfo> query(Page page, XxlJobInfo xxlJobInfo) {
        QueryWrapper<XxlJobInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(xxlJobInfo.getJobDesc()), "job_desc", xxlJobInfo.getJobDesc());
        return this.page(page, queryWrapper);
    }
}