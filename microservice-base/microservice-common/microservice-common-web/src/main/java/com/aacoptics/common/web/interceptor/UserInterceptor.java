package com.aacoptics.common.web.interceptor;

import com.aacoptics.common.core.exception.SystemErrorType;
import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.common.core.vo.Result;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 用户信息拦截器
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    /**
     * 服务间调用token用户信息,格式为json
     * {
     * "user_name":"必须有"
     * "自定义key:"value"
     * }
     */
    public static final String MICROSERVICE_CLIENT_TOKEN_USER = "microservice-client-token-user";
    /**
     * 服务间调用的认证token
     */
    public static final String MICROSERVICE_CLIENT_TOKEN = "microservice-client-token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从网关获取并校验,通过校验就可信任microservice-client-token-user中的信息
//        checkToken(request.getHeader(MICROSERVICE_CLIENT_TOKEN));
        String fromKey = request.getHeader(MICROSERVICE_CLIENT_TOKEN);
        if (fromKey == null || !fromKey.equals("fromGateway")) {
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            response.setStatus(403);
            writer.write(JSONObject.toJSONString(Result.fail(SystemErrorType.NOT_FROM_GATEWAY)));
            return false;
        }
        String userInfoString = StringUtils.defaultIfBlank(request.getHeader(MICROSERVICE_CLIENT_TOKEN_USER), "{}");
        UserContextHolder.getInstance().setContext(new ObjectMapper().readValue(userInfoString, Map.class));
        return true;
    }

//    private boolean checkToken(String token) {
//        if(token == null || !"".equals("fromGateway")){
//            return false;
//        }
//        return true;
//    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContextHolder.getInstance().clear();
    }
}
