package com.aacoptics.ldap.sync.scheduler;

import com.aacoptics.ldap.sync.service.SapOrgEtMdataService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class LdapJobHandle {

    @Resource
    SapOrgEtMdataService sapOrgEtMdataService;

    @XxlJob("syncLdapHandle")
    public void syncLdapHandle() {
        try {
            sapOrgEtMdataService.syncLdapAccount();
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}