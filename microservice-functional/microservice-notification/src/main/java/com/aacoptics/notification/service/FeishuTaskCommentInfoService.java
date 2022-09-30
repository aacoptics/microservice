package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.po.FeishuTaskCommentInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfoHistory;
import com.aacoptics.notification.entity.vo.FeishuTaskEvent;

public interface FeishuTaskCommentInfoService {
    boolean add(FeishuTaskEvent feishuTaskEvent);
}
