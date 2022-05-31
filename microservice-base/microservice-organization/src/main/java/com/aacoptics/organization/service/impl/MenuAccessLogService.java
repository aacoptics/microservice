package com.aacoptics.organization.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.common.web.entity.po.BasePo;
import com.aacoptics.organization.entity.po.MenuAccessLog;
import com.aacoptics.organization.mapper.MenuAccessLogMapper;
import com.aacoptics.organization.service.IMenuAccessLogService;
import com.aacoptics.organization.util.ServletUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MenuAccessLogService extends ServiceImpl<MenuAccessLogMapper, MenuAccessLog> implements IMenuAccessLogService {

    @Resource
    MenuAccessLogMapper menuAccessLogMapper;

    @Override
    public boolean logMenuAccess(MenuAccessLog menuAccessLog) {
        UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
//        String ip = ServletUtils.getIpAddress(ServletUtils.getRequest());

        menuAccessLog.setOs(userAgent.getOs().getName());
        menuAccessLog.setPlatform(userAgent.getPlatform().getName());
        menuAccessLog.setEngine(userAgent.getEngine().getName());
        menuAccessLog.setEngineVersion(userAgent.getEngineVersion());
        menuAccessLog.setAccessTime(LocalDateTime.now());
        menuAccessLog.setUsername(this.getCurrentUsername());
        menuAccessLog.setBrowser(userAgent.getBrowser().getName());
        menuAccessLog.setVersion(userAgent.getVersion());
//        menuAccessLog.setIp(ip);

        return this.save(menuAccessLog);
    }

    @Override
    public Page<MenuAccessLog> getLastWeekAccessLog(Page<MenuAccessLog> iPage) {
        return menuAccessLogMapper.getLastWeekAccessLog(iPage);
    }

    @Override
    public List<MenuAccessLog> getLastMouthTotalCount() {
        return menuAccessLogMapper.getLastMouthTotalCount();
    }

    @Override
    public List<MenuAccessLog> getLastWeekMenuCount() {
        return menuAccessLogMapper.getLastWeekMenuCount();
    }

    /**
     * 获取当前登录用户
     */
    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), BasePo.DEFAULT_USERNAME);
    }
}
