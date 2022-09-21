package com.aacoptics.notification.service.impl;

import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfoHistory;
import com.aacoptics.notification.mapper.FeishuTaskInfoHistoryMapper;
import com.aacoptics.notification.mapper.FeishuTaskInfoMapper;
import com.aacoptics.notification.service.FeishuTaskInfoHistoryService;
import com.aacoptics.notification.service.FeishuTaskInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@Slf4j
public class FeishuTaskInfoHistoryServiceImpl extends ServiceImpl<FeishuTaskInfoHistoryMapper, FeishuTaskInfoHistory> implements FeishuTaskInfoHistoryService {

    @Override
    public boolean add(FeishuTaskInfoHistory feishuTaskInfoHistory) {
        int count = this.count(new QueryWrapper<FeishuTaskInfoHistory>()
                .eq("task_id", feishuTaskInfoHistory.getTaskId())
                .eq("task_status", feishuTaskInfoHistory.getTaskStatus())
                .eq("created_time", feishuTaskInfoHistory.getCreatedTime())
        );
        if (count > 0)
            return true;
        else {
            return this.save(feishuTaskInfoHistory);
        }
    }
}