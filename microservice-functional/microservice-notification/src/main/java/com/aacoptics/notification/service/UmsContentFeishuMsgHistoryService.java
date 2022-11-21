package com.aacoptics.notification.service;

import com.aacoptics.notification.entity.param.UmsContentFeishuMsgHistoryQueryParam;
import com.aacoptics.notification.entity.po.FeishuTaskInfo;
import com.aacoptics.notification.entity.po.FeishuTaskInfoHistory;
import com.aacoptics.notification.entity.po.UmsContentFeishuMsgHistory;
import com.aacoptics.notification.entity.vo.FeishuTaskEvent;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UmsContentFeishuMsgHistoryService extends IService<UmsContentFeishuMsgHistory> {
    boolean add(UmsContentFeishuMsgHistory umsContentFeishuMsgHistory);

    IPage<UmsContentFeishuMsgHistory> query(Page page, UmsContentFeishuMsgHistoryQueryParam umsContentFeishuMsgHistoryQueryParam);

    void deleteMessageByBatchId(String batchId);

    boolean deleteMessageByBatchId(UmsContentFeishuMsgHistory umsContentFeishuMsgHistory);
}
