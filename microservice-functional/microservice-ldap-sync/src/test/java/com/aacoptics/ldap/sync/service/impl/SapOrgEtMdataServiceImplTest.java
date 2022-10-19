package com.aacoptics.ldap.sync.service.impl;


import com.aacoptics.ldap.sync.entity.po.SapOrgEtMdata;
import com.aacoptics.ldap.sync.service.LdapService;
import com.aacoptics.ldap.sync.service.SapOrgEtMdataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.time.LocalDateTime;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SapOrgEtMdataServiceImplTest {

    @Resource
    SapOrgEtMdataService sapOrgEtMdataService;

    @Resource
    LdapService ldapService;

    @Test
    public void listAll() {
        List<SapOrgEtMdata> users = sapOrgEtMdataService.listAll();
        LdapContext context = ldapService.ConnectToLdap();
        if (users.size() > 0) {
            for (SapOrgEtMdata user : users) {
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