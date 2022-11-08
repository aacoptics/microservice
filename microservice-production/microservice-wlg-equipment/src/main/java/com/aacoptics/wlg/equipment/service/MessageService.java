package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.MessageHistory;

public interface MessageService {



    /**
     * 推送点检异常工单
     * XXX（设备编号）存在XXX（保养/点检项目名称）参数异常；参数范围值为XXX（范围开始值）到XXX（范围结束值）；实际参数为XXX（保养/点检实际值）；请注意处理！
     *
     */
    boolean sendInspectionExceptionMessage();

}
