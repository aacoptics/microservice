package com.aacoptics.organization.service;

import com.aacoptics.organization.entity.po.MenuAccessLog;

public interface IMenuAccessLogService {

    /**
     * 新增菜单访问日志
     */
    boolean logMenuAccess(MenuAccessLog menuAccessLog);

}
