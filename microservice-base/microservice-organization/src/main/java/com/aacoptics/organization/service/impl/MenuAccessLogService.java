package com.aacoptics.organization.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.common.web.entity.po.BasePo;
import com.aacoptics.organization.entity.po.MenuAccessLog;
import com.aacoptics.organization.mapper.MenuAccessLogMapper;
import com.aacoptics.organization.service.IMenuAccessLogService;
import com.aacoptics.organization.util.ServletUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class MenuAccessLogService extends ServiceImpl<MenuAccessLogMapper, MenuAccessLog> implements IMenuAccessLogService {

    @Override
    public boolean logMenuAccess(MenuAccessLog menuAccessLog) {
        UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = ServletUtils.getIpAddress(ServletUtils.getRequest());

        menuAccessLog.setOs(userAgent.getOs().getName());
        menuAccessLog.setPlatform(userAgent.getPlatform().getName());
        menuAccessLog.setEngine(userAgent.getEngine().getName());
        menuAccessLog.setEngineVersion(userAgent.getEngineVersion());
        menuAccessLog.setAccessTime(LocalDateTime.now());
        menuAccessLog.setUsername(this.getCurrentUsername());
        menuAccessLog.setBrowser(userAgent.getBrowser().getName());
        menuAccessLog.setVersion(userAgent.getVersion());
        menuAccessLog.setIp(ip);

        return this.save(menuAccessLog);
    }

    /**
     * 获取当前登录用户
     */
    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), BasePo.DEFAULT_USERNAME);
    }
}
