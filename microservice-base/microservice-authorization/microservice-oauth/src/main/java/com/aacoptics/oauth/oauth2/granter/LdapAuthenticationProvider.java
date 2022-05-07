package com.aacoptics.oauth.oauth2.granter;

import com.aacoptics.oauth.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * LDAP登陆provider
 */
public class LdapAuthenticationProvider extends DaoAuthenticationProvider {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    public LdapAuthenticationProvider(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LdapAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            String ldapInfo = userService.GetADInfo(userDetails.getUsername(), presentedPassword);
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword()) && ldapInfo == null) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }
}

//package com.aacoptics.oauth.oauth2.granter;
//
//import com.aacoptics.oauth.entity.SsoResult;
//import com.aacoptics.oauth.provider.LdapProvider;
//import com.aacoptics.oauth.provider.WebServiceUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.*;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.*;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
///**
// * LDAP登陆provider
// */
//public class LdapAuthenticationProvider extends DaoAuthenticationProvider {
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private WebServiceUtil webServiceUtil;
//
//    public LdapAuthenticationProvider(UserDetailsService userDetailsService) {
//        super.setUserDetailsService(userDetailsService);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return LdapAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//
//    @Override
//    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//        if (authentication.getCredentials() == null) {
//            this.logger.debug("Authentication failed: no credentials provided");
//            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//        } else {
//            String presentedPassword = authentication.getCredentials().toString();
//            if(presentedPassword.startsWith("aacOpticsSsoToken=")){
//                String ssoToken = presentedPassword.replace("aacOpticsSsoToken=", "").trim();
//                SsoResult ssoResult = new SsoResult();
//                try {
//                    ssoResult = (SsoResult)webServiceUtil.invokeWebService("http://sso.aacoptics.com:80/UsersService.asmx?WSDL", SsoResult.class, "GetUserInfo", ssoToken);
//                } catch (Exception e) {
//                    ssoResult.setStatus("0");
//                    e.printStackTrace();
//                }
//                if(!ssoResult.getStatus().equals("1")){
//                    this.logger.debug("Authentication failed: token does not match");
//                    throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad token"));
//                }
//            }else {
//                String ldapInfo = LdapProvider.GetADInfo("ldap://cz.ldap.aacoptics.com:389", userDetails.getUsername(), presentedPassword);
//                if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword()) && ldapInfo == null) {
//                    this.logger.debug("Authentication failed: password does not match stored value");
//                    throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//                }
//            }
//        }
//    }
//}