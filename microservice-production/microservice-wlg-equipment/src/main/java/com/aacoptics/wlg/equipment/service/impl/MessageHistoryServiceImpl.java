package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.po.MessageHistory;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.MessageHistoryMapper;
import com.aacoptics.wlg.equipment.service.MessageHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class MessageHistoryServiceImpl extends ServiceImpl<MessageHistoryMapper, MessageHistory> implements MessageHistoryService {

    @Resource
    MessageHistoryMapper messageHistoryMapper;

    @Override
    public boolean add(MessageHistory messageHistory) {
        boolean isSuccess = this.save(messageHistory);
        if(isSuccess == false)
        {
            throw new BusinessException("新增维修消息推送历史失败");
        }

        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(MessageHistory messageHistory) {

        boolean isSuccess = this.updateById(messageHistory);
        return isSuccess;
    }


    @Override
    public MessageHistory get(Long id) {
        MessageHistory messageHistory = this.getById(id);
        if (Objects.isNull(messageHistory)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return messageHistory;
    }

}
