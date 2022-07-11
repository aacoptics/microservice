package com.aacoptics.mold.toollife.service;

import com.aacoptics.mold.toollife.entity.ToolInfoHistory;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ToolInfoHistoryService extends IService<ToolInfoHistory> {
    boolean addToolInfoHistory(ToolInfoHistory toolInfoHistory);
}
