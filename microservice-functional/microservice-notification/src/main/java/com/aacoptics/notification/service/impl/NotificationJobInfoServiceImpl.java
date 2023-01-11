package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.NotificationJobInfo;
import com.aacoptics.notification.mapper.NotificationJobInfoMapper;
import com.aacoptics.notification.service.NotificationJobInfoService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
@Slf4j
@DS("msg_db")
public class NotificationJobInfoServiceImpl extends ServiceImpl<NotificationJobInfoMapper, NotificationJobInfo> implements NotificationJobInfoService {

    @Resource
    NotificationJobInfoMapper notificationJobInfoMapper;

    @Override
    public boolean add(NotificationJobInfo notificationJobInfo) {
        String jobNo = notificationJobInfoMapper.getMaxNo();
        String notificationNo = String.format("%03d", Integer.parseInt(jobNo) + 1);
        notificationJobInfo.setNotificationNo(notificationNo);
        return this.save(notificationJobInfo);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean deleteByXxlJobId(Integer id){
        QueryWrapper<NotificationJobInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("xxl_job_id", id);
        return notificationJobInfoMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean updateByXxlJobId(Integer id, NotificationJobInfo notificationJobInfo){
        UpdateWrapper<NotificationJobInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("xxl_job_id", id);
        return this.update(notificationJobInfo, wrapper);
    }

    @Override
    public boolean update(NotificationJobInfo notificationJobInfo) {
        return this.updateById(notificationJobInfo);
    }
}