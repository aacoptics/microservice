package com.aacoptics.ldap.sync.service;


import com.aacoptics.ldap.sync.entity.po.SapOrgEtMdata;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.util.List;

public interface LdapService {

    void CreateAdUser(SapOrgEtMdata userInfo, LdapContext context);

    void CreateAdUser(LdapContext context, Boolean needCreateMail, String userName, String firstName, String lastName, String pyName, String deptName, String userDn, String wifiGroup, String printGroup);

    void AddGroup(String groupDn, String userDn, LdapContext context);

    String GetMailNickName(String pyName, LdapContext context);

    void EnableMailBox(LdapContext context, String sAMAccountName, String mailNickName);

    void deleteUser(String sAMAccountName,  LdapContext ctx) throws NamingException;

    LdapContext ConnectToLdap();

    void DisableAdAccount(String jobNumber, LdapContext context);
}
