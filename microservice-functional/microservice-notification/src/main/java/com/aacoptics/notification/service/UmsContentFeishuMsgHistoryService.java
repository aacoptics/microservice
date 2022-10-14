package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.po.UmsContentFeishuMsgHistory;
import com.aacoptics.notification.entity.vo.FeishuTaskEvent;

public interface UmsContentFeishuMsgHistoryService {
    boolean add(UmsContentFeishuMsgHistory umsContentFeishuMsgHistory);
}
