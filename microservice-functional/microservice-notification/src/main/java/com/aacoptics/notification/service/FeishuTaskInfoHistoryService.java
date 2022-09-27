package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfoHistory;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FeishuTaskInfoHistoryService extends IService<FeishuTaskInfoHistory> {
    boolean add(FeishuTaskInfoHistory feishuTaskInfoHistory);
}
