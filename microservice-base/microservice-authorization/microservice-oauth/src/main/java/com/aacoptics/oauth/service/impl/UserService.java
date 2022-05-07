package com.aacoptics.oauth.service.impl;

import com.aacoptics.oauth.entity.User;
import com.aacoptics.oauth.provider.OrganizationProvider;
import com.aacoptics.oauth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@Service
public class UserService implements IUserService {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Value("${ldapInfo.DOMAIN_NAME}")
    private String DOMAIN_NAME;

    @Value("${ldapInfo.DOMAIN_ROOT}")
    private String DOMAIN_ROOT;

    @Value("${ldapInfo.DOMAIN_URL}")
    private String DOMAIN_URL;

    @Override
    public User getByUniqueId(String uniqueId) {
        return organizationProvider.getUserByUniqueId(uniqueId).getData();
    }

    @Override
    public String GetADInfo(String name, String pwd) {
        String result = "";
        Hashtable HashEnv = new Hashtable();
        HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
        HashEnv.put(Context.SECURITY_PRINCIPAL, DOMAIN_NAME + "\\" + name); // AD User
        HashEnv.put(Context.SECURITY_CREDENTIALS, pwd); // AD Password
        HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
        HashEnv.put(Context.PROVIDER_URL, DOMAIN_URL);
        try {
            LdapContext ctx = new InitialLdapContext(HashEnv, null);
            // 域节点
            String searchBase = DOMAIN_ROOT;
            // LDAP搜索过滤器类
            String searchFilter = "SAMAccountName=" + name;
            // 搜索控制器
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
            NamingEnumeration answer = ctx.search(searchBase, searchFilter, sc);
            while (answer.hasMoreElements()) {// 遍历结果集
                SearchResult sr = (SearchResult) answer.next();// 得到符合搜索条件的DN
                result = sr.getName();
            }
            ctx.close();
        } catch (NamingException e) {
            return null;
        }
        return result;
    }
}