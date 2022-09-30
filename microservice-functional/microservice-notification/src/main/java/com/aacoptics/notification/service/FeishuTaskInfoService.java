package com.aacoptics.notification.service;

import cn.hutool.json.JSONObject;
import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.vo.FeishuTaskEvent;
import com.aacoptics.notification.entity.vo.FeishuTaskVo;

public interface FeishuTaskInfoService {
    boolean add(FeishuTaskInfo feishuTaskInfo);

    boolean updateOrInsert(FeishuTaskEvent feishuTaskEvent);
}
