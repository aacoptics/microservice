package com.aacoptics.wlg.equipment.service;

import com.aacoptics.wlg.equipment.entity.po.MessageHistory;

public interface MessageHistoryService {




    /**
     * 更新消息推送历史信息
     *
     * @param messageHistory
     */
    boolean update(MessageHistory messageHistory);

    /**
     * 根据id删除消息推送历史
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 新增消息推送历史
     *
     * @param messageHistory
     * @return
     */
    boolean add(MessageHistory messageHistory);



    /**
     * 获取消息推送历史
     *
     * @param id
     * @return
     */
    MessageHistory get(Long id);




}
