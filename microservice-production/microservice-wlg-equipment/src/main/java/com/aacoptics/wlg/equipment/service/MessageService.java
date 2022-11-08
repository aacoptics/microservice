package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.MessageHistory;

public interface MessageService {



    /**
     * 推送点检异常工单
     *
     */
    boolean sendInspectionExceptionMessage();

}
