package com.aacoptics.ldap.sync.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aacoptics.ldap.sync.entity.po.SapOrgEtMdata;
import com.aacoptics.ldap.sync.mapper.SapOrgEtMdataMapper;
import com.aacoptics.ldap.sync.service.LdapService;
import com.aacoptics.ldap.sync.service.SapOrgEtMdataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class SapOrgEtMdataServiceImpl extends ServiceImpl<SapOrgEtMdataMapper, SapOrgEtMdata> implements SapOrgEtMdataService {

    @Resource
    LdapService ldapService;

    @Override
    public List<SapOrgEtMdata> listAll(){
        return this.list();
    }

    @Override
    public void syncLdapAccount(){
        List<SapOrgEtMdata> users = this.listAll();
        LdapContext context = ldapService.ConnectToLdap();
        if (users.size() > 0) {
            for (SapOrgEtMdata user : users) {
                if(user.getPernr().equals("JamesWang"))
                    continue;
                ldapService.CreateAdUser(user, context);
            }
        }
        try {
            context.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

}
