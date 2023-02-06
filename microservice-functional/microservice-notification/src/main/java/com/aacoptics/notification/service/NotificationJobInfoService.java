package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.param.RobotQueryParam;
import com.aacoptics.notification.entity.po.NotificationJobInfo;
import com.aacoptics.notification.entity.po.Robot;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface NotificationJobInfoService {

    boolean add(NotificationJobInfo notificationJobInfo);

    boolean delete(Long id);

    boolean deleteByXxlJobId(Integer id);

    boolean updateApproveStatus(String approveId, Integer status);

    boolean updateByXxlJobId(Integer id, NotificationJobInfo notificationJobInfo);

    boolean update(NotificationJobInfo notificationJobInfo);
}
