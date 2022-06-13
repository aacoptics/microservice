package com.aacoptics.notification.service;


import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.entity.param.XxlJobInfoQueryParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


public interface XxlJobInfoService extends IService<XxlJobInfo> {

    boolean add(XxlJobInfo xxlJobInfo);

    boolean update(XxlJobInfo xxlJobInfo);

    boolean delete(Integer id);

    IPage<XxlJobInfo> query(Page page, XxlJobInfoQueryParam xxlJobInfoQueryParam);
}