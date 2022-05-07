package com.aacoptics.oauth.oauth2.granter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * 手机验证码登陆Token认证类
 */
public class LdapAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public LdapAuthenticationToken(Authentication authenticationToken) {
        super(authenticationToken.getPrincipal(), authenticationToken.getCredentials());
    }
}
