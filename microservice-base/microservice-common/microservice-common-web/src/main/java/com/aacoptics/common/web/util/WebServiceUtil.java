/* ====================================================================================================
 * Copyright© 2021  诚瑞光学  All Rights Reserved
 *
 * ====================================================================================================
 * Change Log
 * ====================================================================================================
 * 2021-03-11     诚瑞光学      [Init] 调用WebService工具类.
 * ==================================================================================================== */
package com.aacoptics.common.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.stereotype.Component;

/**
 * <p>调用WebService工具类</p>
 *
 * @author 诚瑞光学
 * @version 1.0.0
 * @since jdk 1.8
 */
@SuppressWarnings("unused")
@Component
public class WebServiceUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 调用指定的Web Services, 适用于响应结果为Json字符串
     *
     * @param wsdl       待调用的Web Services的wsdl
     * @param classType  调用返回的类型
     * @param methodName 调用的方法
     * @param params     调用的参数
     * @return 调用结果
     */
    public Object invokeWebService(String wsdl, Class<?> classType, String methodName, Object... params) throws Exception {
        // 创建动态客户端
        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        try (Client client = jaxWsDynamicClientFactory.createClient(wsdl)) {
            Object[] result = client.invoke(methodName, params);
            String resultString = (String) result[0];
            return objectMapper.readValue(resultString, classType);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 调用指定的Web Services, 适用于响应结果为数组
     *
     * @param wsdl       待调用的Web Services的wsdl
     * @param methodName 调用的方法
     * @param params     调用的参数
     * @return 调用结果
     */
    public Object[] invokeWebService(String wsdl, String methodName, Object... params) throws Exception {
        // 创建动态客户端
        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        try (Client client = jaxWsDynamicClientFactory.createClient(wsdl)) {
            return client.invoke(methodName, params);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
