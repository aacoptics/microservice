package com.aacoptics.ldap.sync.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.ldap.sync.config.LdapConfig;
import com.aacoptics.ldap.sync.constant.CommonConstants;
import com.aacoptics.ldap.sync.entity.po.SapOrgEtMdata;
import com.aacoptics.ldap.sync.service.LdapService;
import com.aacoptics.ldap.sync.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class LdapServiceImpl implements LdapService {

    @Resource
    LdapConfig ldapConfig;

    @Override
    public LdapContext ConnectToLdap() {
        System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.ldap.factory.socket", "com.aacoptics.ldap.sync.ldap.DummySSLSocketFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldapConfig.getAdminName());
        env.put(Context.SECURITY_CREDENTIALS, ldapConfig.getAdminPass());
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        env.put(Context.PROVIDER_URL, ldapConfig.getDomainUrl());
        try {
            return new InitialLdapContext(env, null);
        } catch (NamingException e) {
            log.error("连接AD域失败" + e.getMessage());
            return null;
        }
    }

    @Override
    public void CreateAdUser(SapOrgEtMdata userInfo, LdapContext context) {
        try {
            if (StrUtil.isBlank(userInfo.getStat2()))
                return;

            if (!userInfo.getStat2().equals("3")) {
                DisableAdAccount(userInfo.getPernr(), context);
                return;
            }
            if (!CommonConstants.AREA_MAP.containsKey(userInfo.getBtrtl())) {
                log.error("找不到对应的地区");
                return;
            }
            String userName = userInfo.getPernr();
            String firstName = userInfo.getVorna();
            String lastName = userInfo.getNachn();
            String cnValue = lastName + firstName;
            String pyName = userInfo.getName2();

            List<String> deptList = new ArrayList<>();
            if (!StrUtil.isBlank(userInfo.getLev0DeptNo()) && !userInfo.getLev0DeptNo().equals("00000000")) {
                deptList.add(userInfo.getLev0DeptName());
                if (!StrUtil.isBlank(userInfo.getLev1DeptNo()) && !userInfo.getLev1DeptNo().equals("00000000")) {
                    deptList.add(userInfo.getLev1DeptName());
                    if (!StrUtil.isBlank(userInfo.getLev2DeptNo()) && !userInfo.getLev2DeptNo().equals("00000000")) {
                        deptList.add(userInfo.getLev2DeptName());
                        if (!StrUtil.isBlank(userInfo.getLev3DeptNo()) && !userInfo.getLev3DeptNo().equals("00000000")) {
                            deptList.add(userInfo.getLev3DeptName());
                            if (!StrUtil.isBlank(userInfo.getLev4DeptNo()) && !userInfo.getLev4DeptNo().equals("00000000")) {
                                deptList.add(userInfo.getLev4DeptName());
                                if (!StrUtil.isBlank(userInfo.getLev5DeptNo()) && !userInfo.getLev5DeptNo().equals("00000000")) {
                                    deptList.add(userInfo.getLev5DeptName());
                                }
                            }
                        }
                    }
                }
            }

            String deptName = String.join("_", deptList.toArray(new String[deptList.size()]));

            String userDn;
            if (userInfo.getBtrtl().equals("1010") ||
                    userInfo.getBtrtl().equals("1009") ||
                    userInfo.getBtrtl().equals("1015") ||
                    userInfo.getBtrtl().equals("1021")) {
                userDn = "cn=" + cnValue + "," + CommonConstants.AREA_MAP.get(userInfo.getBtrtl()) + ",OU=Non-B_Accounts,OU=OF_Users,OU=AACOPTICS_Users," + ldapConfig.getDomainRoot();
            } else {
                userDn = "cn=" + cnValue + "," + CommonConstants.DEPT_MAP.getOrDefault(userInfo.getLev1DeptNo(), "OU=HR") + "," + CommonConstants.AREA_MAP.get(userInfo.getBtrtl()) + ",OU=Non-B_Accounts,OU=OF_Users,OU=AACOPTICS_Users," + ldapConfig.getDomainRoot();
            }
            String printGroup;
            String wifiGroup = CommonConstants.WIFI_NEMPLOYEE;
            if (userInfo.getBtrtl().equals("1001") || userInfo.getBtrtl().equals("1002")) {
                printGroup = CommonConstants.PRINT_SHENZHEN;
            } else if (userInfo.getBtrtl().equals("1007")) {
                printGroup = CommonConstants.PRINT_SHUYANG;
            } else {
                printGroup = CommonConstants.PRINT_MAIN;
            }
            boolean needCreateMail = false;
            if (!Arrays.asList(new String[]{"30", "40", "70", "80"}).contains(userInfo.getPersk())) {
                needCreateMail = true;
            }
            CreateAdUser(context, needCreateMail, userName, firstName, lastName, pyName, deptName, userDn, wifiGroup, printGroup);
        } catch (Exception err) {
            log.error(err.getMessage());
        }
    }

    @Override
    public void CreateAdUser(LdapContext context, Boolean needCreateMail, String userName, String firstName, String lastName, String pyName, String deptName, String userDn, String wifiGroup, String printGroup) {
        if (!CheckIsExist(userName, context)) {
            log.warn(userName + "用户已存在！");
            return;
        }
        String mailNickname = GetMailNickName(pyName, context);
        String email = mailNickname + "@" + ldapConfig.getDomainName();
        Attributes container = new BasicAttributes();
        Attribute objClasses = new BasicAttribute("objectClass");
        objClasses.add("top");
        objClasses.add("person");
        objClasses.add("organizationalPerson");
        objClasses.add("user");
        String cnValue = lastName + firstName;
        Attribute cn = new BasicAttribute("cn", cnValue);
        Attribute sAMAccountName = new BasicAttribute("sAMAccountName", userName);
        Attribute principalName = new BasicAttribute("userPrincipalName", String.format("%s@%s", userName, ldapConfig.getDomainName()));
        Attribute givenName = new BasicAttribute("givenName", firstName);
        Attribute sn = new BasicAttribute("sn", lastName);
        Attribute mail = new BasicAttribute("mail", email);
        Attribute description = new BasicAttribute("description", String.format("%s(%s)", userName, deptName));
        Attribute displayName = new BasicAttribute("displayName", pyName);
        Attribute employeeID = new BasicAttribute("employeeID", userName);
        Attribute employeeNumber = new BasicAttribute("employeeNumber", userName);
        Attribute extensionAttribute2 = new BasicAttribute("extensionAttribute2", "user");
        Attribute department = new BasicAttribute("department", deptName);
        Attribute userAccountControl = new BasicAttribute("userAccountControl", Integer.toString(512));
        container.put(objClasses);
        container.put(sAMAccountName);
        container.put(principalName);
        container.put(cn);
        container.put(sn);
        container.put(givenName);
        container.put(mail);
        container.put(description);
        container.put(displayName);
        container.put(employeeID);
        container.put(employeeNumber);
        container.put(extensionAttribute2);
        container.put(department);
        byte[] quotedPasswordBytes;
        quotedPasswordBytes = ('"' + "123456!a" + '"').getBytes(StandardCharsets.UTF_16LE);
        container.put(new BasicAttribute("unicodePwd", quotedPasswordBytes));
        container.put(userAccountControl);
        try {
            context.createSubcontext(userDn, container);
            AddGroup(printGroup, userDn, context);
            if (needCreateMail) {
                AddGroup(wifiGroup, userDn, context);
                EnableMailBox(context, userName, mailNickname);
            }
            log.info("创建用户成功, USERDN:" + userDn);
        } catch (Exception e) {
            log.error("创建用户失败" + userDn + e.getMessage());
        }
    }

    @Override
    public void EnableMailBox(LdapContext context, String sAMAccountName, String mailNickName) {
        String database = GetMailDatabase(context);
        Map<String, Object> userMailInfo = new HashMap<>();
        userMailInfo.put("sAMAccountName", sAMAccountName);
        userMailInfo.put("mailNickname", mailNickName);
        userMailInfo.put("database", database);
        EnableMailBox(userMailInfo);
    }

    public void EnableMailBox(Map<String, Object> params) {
        String result = HttpClientUtil.doPost(ldapConfig.getApiUser(), ldapConfig.getApiPassword(), ldapConfig.getApiUrl(), params);
        if (result.equals("\"true\"")) {
            log.info("启用" + params.get("sAMAccountName")
                    + "成功！ 邮箱别名"
                    + params.get("mailNickname")
                    + "，创建在" + params.get("database") + "数据库中！");
        } else {
            log.error(result);
        }
    }

    @Override
    public void DisableAdAccount(String jobNumber, LdapContext context) {
        if (jobNumber.equals("60055111") || jobNumber.equals("60057699")
                || jobNumber.equals(""))
            return;
        if (context != null) {
            try {
                deleteUser(jobNumber, context);
                log.info("删除账号" + jobNumber + "成功！");
            } catch (NamingException e) {
                log.error("删除账号失败" + e.getMessage());
            }
        }
    }

    @Override
    public void AddGroup(String groupDn, String userDn, LdapContext context) {
        if (context != null) {
            try {
                // 域节点
                String searchBase = ldapConfig.getDomainRoot();
                String searchFilter = "(distinguishedName=" + groupDn + ")";    //查询域帐号
                // 创建搜索控制器
                SearchControls searchCtls = new SearchControls();
                String returnedAtts[] = {"member"};
                searchCtls.setReturningAttributes(returnedAtts); //设置指定返回的字段，不设置则返回全部
                //  设置搜索范围 深度
                searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
                NamingEnumeration answer = context.search(searchBase, searchFilter, searchCtls);
                // 初始化搜索结果数为0
                while (answer.hasMoreElements()) {// 遍历结果集
                    SearchResult sr = (SearchResult) answer.next();// 得到符合搜索条件的DN
                    Attributes Attrs = sr.getAttributes();// 得到符合条件的属性集
                    Attribute members = Attrs.get("member");
                    if (members == null) {
                        continue;
                    } else {
                        if (members.contains(userDn)) {
                            log.warn(userDn + "已存在于" + groupDn);
                        } else {
                            members.add(userDn);
                            Attributes attributes = new BasicAttributes(true);
                            attributes.put(members);
                            context.modifyAttributes(groupDn, DirContext.REPLACE_ATTRIBUTE, attributes);
                            log.info(userDn + "添加成功" + groupDn + "  " + members.size());
                        }
                    }
                }
            } catch (NamingException e) {
                log.error("添加组失败" + e.getMessage());
            }
        }
    }

    @Override
    public String GetMailNickName(String pyName, LdapContext context) {
        String pyNameNoSpace = pyName.replaceAll("\\s*", "");
        Integer i = 2;
        String pyNameFinal;
        if (CheckMailNickName(pyNameNoSpace, context)) {
            pyNameFinal = pyNameNoSpace;
        } else {
            while (!CheckMailNickName(pyNameNoSpace + i, context)) {
                i++;
            }
            pyNameFinal = pyNameNoSpace + i;
        }
        return pyNameFinal;
    }

    public Boolean CheckMailNickName(String pyName, LdapContext context) {
        if (context != null) {
            try {
                // 域节点
                String searchBase = ldapConfig.getDomainRoot();
                String searchFilter = "(mail=" + pyName + "@aacoptics.com)";    //查询域帐号
                // 创建搜索控制器
                SearchControls searchCtls = new SearchControls();
                String returnedAtts[] = {"sAMAccountName"};
                searchCtls.setReturningAttributes(returnedAtts); //设置指定返回的字段，不设置则返回全部
                //  设置搜索范围 深度
                searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
                NamingEnumeration answer = context.search(searchBase, searchFilter, searchCtls);
                // 初始化搜索结果数为0
                if (answer.hasMoreElements()) {
                    return false;
                }
                return true;
            } catch (NamingException e) {
                log.error("检查邮箱唯一性失败" + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public Boolean CheckIsExist(String pernr, LdapContext context) {
        if (context != null) {
            try {
                // 域节点
                String searchBase = ldapConfig.getDomainRoot();
                String searchFilter = "(|(sAMAccountName=" + pernr + ")(employeeID=" + pernr + ")(employeeNumber=" + pernr + "))";    //查询域帐号
                // 创建搜索控制器
                SearchControls searchCtls = new SearchControls();
                String returnedAtts[] = {"sAMAccountName"};
                searchCtls.setReturningAttributes(returnedAtts); //设置指定返回的字段，不设置则返回全部
                //  设置搜索范围 深度
                searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
                NamingEnumeration answer = context.search(searchBase, searchFilter, searchCtls);
                // 初始化搜索结果数为0
                if (answer.hasMoreElements()) {
                    return false;
                }
                return true;
            } catch (NamingException e) {
                log.error("检查人员唯一性失败" + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public String GetMailDatabase(LdapContext context) {
        if (context != null) {
            try {
                // 域节点
                String searchBase = ldapConfig.getMailDomainRoot();

                String searchFilter = "(|" + "(distinguishedName=" + CommonConstants.MAIL_DATABASE_1 + ")" +
                        "(distinguishedName=" + CommonConstants.MAIL_DATABASE_2 + ")" +
                        "(distinguishedName=" + CommonConstants.MAIL_DATABASE_3 + ")" +
                        "(distinguishedName=" + CommonConstants.MAIL_DATABASE_4 + ")" +
                        "(distinguishedName=" + CommonConstants.MAIL_DATABASE_5 + ")" + ")";    //查询域帐号

                // 创建搜索控制器
                SearchControls searchCtls = new SearchControls();
                String returnedAtts[] = {"homeMDBBL", "cn"};
                searchCtls.setReturningAttributes(returnedAtts); //设置指定返回的字段，不设置则返回全部
                //  设置搜索范围 深度
                searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
                NamingEnumeration answer = context.search(searchBase, searchFilter, searchCtls);
                // 初始化搜索结果数为0
                String finalDb = null;
                Integer tempInt = -1;
                while (answer.hasMoreElements()) {// 遍历结果集
                    SearchResult sr = (SearchResult) answer.next();// 得到符合搜索条件的DN
                    String dn = sr.getName();
                    Attributes Attrs = sr.getAttributes();// 得到符合条件的属性集
                    Attribute members = Attrs.get("homeMDBBL");
                    Attribute cn = Attrs.get("cn");
                    if (tempInt == -1) {
                        tempInt = members.size();
                        finalDb = cn.get().toString();
                    } else {
                        if (tempInt > members.size()) {
                            tempInt = members.size();
                            finalDb = cn.get().toString();
                        }
                    }
                }
                return finalDb;
            } catch (NamingException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String sAMAccountName, LdapContext ctx) throws NamingException {
        String searchBase = ldapConfig.getDomainRoot();

        String searchFilter = "(sAMAccountName=" + sAMAccountName + ")";

        // 创建搜索控制器
        SearchControls searchCtls = new SearchControls();
        String returnedAtts[] = {"sAMAccountName", "cn"};
        searchCtls.setReturningAttributes(returnedAtts); //设置指定返回的字段，不设置则返回全部
        //  设置搜索范围 深度
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
        NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
        // 初始化搜索结果数为0
        while (answer.hasMoreElements()) {// 遍历结果集
            SearchResult sr = (SearchResult) answer.next();// 得到符合搜索条件的DN
            ctx.destroySubcontext(sr.getName() + "," + ldapConfig.getDomainRoot());
        }
    }
}