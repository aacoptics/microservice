package com.aacoptics.oauth.config;

import com.aacoptics.oauth.exception.CustomWebResponseExceptionTranslator;
import com.aacoptics.oauth.oauth2.enhancer.CustomTokenEnhancer;
import com.aacoptics.oauth.oauth2.granter.LdapTokenGranter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    public RedisConnectionFactory redisConnectionFactory;
    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    @Qualifier("userDetailsService")
    UserDetailsService userDetailsService;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    /**
     * jwt ??????????????????
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        // ?????????client????????????header???body???
        oauthServer.allowFormAuthenticationForClients();
        oauthServer.tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // ??????????????????????????????????????????????????????oauth_client_details???
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // ??????token???????????????????????????tokenServices?????????,?????????????????????????????????????????????TokenStore???TokenGranter???OAuth2RequestFactory
        endpoints.tokenStore(tokenStore())
                .authorizationCodeServices(authorizationCodeServices())
                .approvalStore(approvalStore())
                .exceptionTranslator(customExceptionTranslator())
                .tokenEnhancer(tokenEnhancerChain())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenGranter(tokenGranter(endpoints));

    }

    /**
     * ?????????OAuth2????????????
     *
     * @return CustomWebResponseExceptionTranslator
     */
    @Bean
    public WebResponseExceptionTranslator<OAuth2Exception> customExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

    /**
     * ???????????????????????????
     *
     * @return JdbcApprovalStore
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * ?????????????????????????????????code
     *
     * @return JdbcAuthorizationCodeServices
     */
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        // ??????????????????????????????????????????jdbc?????????oauth_code???
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * token????????????
     *
     * @return JwtTokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        //return new JwtTokenStore(accessTokenConverter());
        //??????redis??????token
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //??????redis token??????????????????
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }

    /**
     * ?????????token
     *
     * @return tokenEnhancerChain
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), accessTokenConverter()));
        return tokenEnhancerChain;
    }

    /**
     * jwt token???????????????
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }

    /**
     * ??????????????????granter,????????????????????????
     *
     * @param endpoints
     * @return
     * @auth joe_chen
     */
    public TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = Lists.newArrayList(endpoints.getTokenGranter());
        granters.add(new LdapTokenGranter(
                authenticationManager,
                endpoints.getTokenServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(granters);
    }
}