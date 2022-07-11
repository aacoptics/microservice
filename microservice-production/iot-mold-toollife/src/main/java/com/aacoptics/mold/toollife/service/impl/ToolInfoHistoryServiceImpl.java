package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.dao.ToolInfoHistoryMapper;
import com.aacoptics.mold.toollife.entity.ToolInfoHistory;
import com.aacoptics.mold.toollife.service.ToolInfoHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Slf4j
public class ToolInfoHistoryServiceImpl extends ServiceImpl<ToolInfoHistoryMapper, ToolInfoHistory> implements ToolInfoHistoryService {

    @Transactional
    @Override
    public boolean addToolInfoHistory(ToolInfoHistory toolInfoHistory) {
        try{
            this.save(toolInfoHistory);
            return true;
        } catch(Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
