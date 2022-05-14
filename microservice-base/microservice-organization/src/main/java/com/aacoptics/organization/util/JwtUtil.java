package com.aacoptics.organization.util;

import cn.hutool.core.util.ObjectUtil;
import com.aacoptics.organization.constant.CommonConstant;
import com.aacoptics.organization.entity.po.Resource;
import com.aacoptics.organization.entity.po.User;
import com.aacoptics.organization.exception.AuthenticationException;
import com.aacoptics.organization.exception.OrganizationErrorType;
import com.aacoptics.organization.service.impl.ResourceService;
import com.aacoptics.organization.service.impl.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * JWT工具类
 **/
@Component
public class JwtUtil {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String realName, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .withClaim("realName", realName)
                    .build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getRealName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("realName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,24Hour后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String realName, String secret) {
        Date date = new Date(System.currentTimeMillis() + CommonConstant.TOKEN_EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withClaim("realName", realName)
                .withExpiresAt(date)
                .sign(algorithm);

    }

    /**
     * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
     * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
     * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
     *
     * @param userName userName
     * @param passWord passWord
     * @return boolean
     */
    public boolean jwtTokenRefresh(String token, String userName, String realName, String passWord) {
        Object tokenObj = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token);
        if (ObjectUtil.isNotNull(tokenObj)) {
            String cacheToken = String.valueOf(tokenObj);
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, realName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, realName, passWord);
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, CommonConstant.TOKEN_EXPIRE_TIME / 1000);
            } else {
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, CommonConstant.TOKEN_EXPIRE_TIME / 1000);
            }
            return true;
        }
        return false;
    }

    /**
     * 校验token的有效性
     *
     * @param token token
     */
    public User checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        String realName = JwtUtil.getRealName(token);
        if (username == null) {
            throw new AuthenticationException(OrganizationErrorType.TOKEN_NOT_FOUND);
        }
        // 查询用户信息
        User loginUser = new User();
        User sysUser = userService.getByUniqueId(username);
        if (sysUser == null) {
            throw new AuthenticationException(OrganizationErrorType.USER_NOT_FOUND);
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, realName, sysUser.getPassword())) {
            throw new AuthenticationException(OrganizationErrorType.TOKEN_INVALID);
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    public boolean checkUserPermission(String userName, String url) {
        List<Resource> permissionSet = resourceService.listByUserName(userName);
        return permissionSet.contains(url);
    }
}