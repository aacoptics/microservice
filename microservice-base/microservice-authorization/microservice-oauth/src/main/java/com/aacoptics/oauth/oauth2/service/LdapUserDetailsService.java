package com.aacoptics.oauth.oauth2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * LDAP登陆, 用户相关获取
 */
@Slf4j
@Service("ldapUserDetailsService")
public class LdapUserDetailsService extends CustomUserDetailsService {
}