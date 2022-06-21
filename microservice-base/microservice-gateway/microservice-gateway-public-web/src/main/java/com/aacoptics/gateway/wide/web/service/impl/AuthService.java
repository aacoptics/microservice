package com.aacoptics.gateway.wide.web.service.impl;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gateway.wide.web.provider.AuthProvider;
import com.aacoptics.gateway.wide.web.service.IAuthService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Stream;

import static com.aacoptics.gateway.wide.web.exception.PermissionErrorType.*;

@RefreshScope
@Service
@Slf4j
public class AuthService implements IAuthService {
    /**
     * Authorization认证开头是"bearer "
     */
    private static final String BEARER = "Bearer ";

    @Resource
    private AuthProvider authProvider;

    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    /**
     * 不需要网关签权的url配置(/oauth,/open)
     * 默认/oauth开头是不需要的
     */
    @Value("${gate.ignore.authentication.startWith}")
    private String ignoreUrls = "/oauth";

    @Override
    public Result authenticate(String authentication, String url, String method) {
        return authProvider.auth(authentication, url, method);
    }

    @Override
    public boolean ignoreAuthentication(String url, String method) {
        boolean springConfigResult = Stream.of(this.ignoreUrls.split(",")).anyMatch(ignoreUrl -> url.startsWith(StringUtils.trim(ignoreUrl)));
        if (springConfigResult) return true;

        Result res = authProvider.ignoreAuthentication(url, method);
        //调用签权服务看用户是否有权限，若有权限进入下一个filter
        if (res.isSuccess())
            return (boolean) res.getData();
        return false;
    }

    @Override
    public boolean hasPermission(Result authResult) {
        log.debug("签权结果:{}", authResult.getData());
        return authResult.isSuccess() && (boolean) authResult.getData();
    }

    @Override
    public Result hasPermission(String authentication, String url, String method) {
        // 如果请求未携带token信息, 直接权限
        if (StringUtils.isBlank(authentication) || !authentication.startsWith(BEARER)) {
            log.error("user token is null");
            return Result.fail(TOKEN_NOT_FOUND);
        }
        //token是否有效，在网关进行校验，无效/过期等
        Result res = invalidJwtAccessToken(authentication);
        if (res.isFail()) {
            return res;
        }
        //从认证服务获取是否有权限,远程调用
        return authenticate(authentication, url, method);
    }

    @Override
    public Jws<Claims> getJwt(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }
        return Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(signingKey.getBytes()) //设置签名的秘钥
                .parseClaimsJws(jwtToken);
    }

    @Override
    public Result invalidJwtAccessToken(String authentication) {
        // 是否无效true表示无效
        boolean invalid = Boolean.TRUE;
        try {
            getJwt(authentication);
            invalid = Boolean.FALSE;
            return Result.success();
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException ex) {
            log.error("user token error :{}", ex.getMessage());
            if (ex instanceof SignatureException) {
                return Result.fail(TOKEN_INVALID);
            } else if (ex instanceof ExpiredJwtException) {
                return Result.fail(TOKEN_EXPIRE);
            } else {
                return Result.fail(ex.getMessage());
            }
        }
    }
}