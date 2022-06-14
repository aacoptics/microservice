package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.XxlGroupInfo;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.mapper.XxlGroupInfoMapper;
import com.aacoptics.notification.mapper.XxlJobInfoMapper;
import com.aacoptics.notification.service.XxlGroupInfoService;
import com.aacoptics.notification.service.XxlJobInfoService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
@DS("msg_db")
public class XxlGroupInfoServiceImpl extends ServiceImpl<XxlGroupInfoMapper, XxlGroupInfo> implements XxlGroupInfoService {
}