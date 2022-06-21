package com.aacoptics.gateway.wide.web.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gateway.wide.web.exception.PermissionErrorType;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "microservice-perms", fallbackFactory = AuthProvider.AuthProviderFallbackFactory.class)
public interface AuthProvider {
    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication
     * @param url
     * @return <pre>
     * Result:
     * {
     *   code:"000000"
     *   mesg:"请求成功"
     *   data: true/false
     * }
     * </pre>
     */
    @PostMapping(value = "/auth/permission")
    Result auth(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication, @RequestParam("url") String url, @RequestParam("method") String method);

    @PostMapping(value = "/auth/ignoreAuthentication")
    Result ignoreAuthentication(@RequestParam("url") String url, @RequestParam("method") String method);

    @Component
    class AuthProviderFallbackFactory implements FallbackFactory<AuthProvider> {

        @SuppressWarnings("unchecked")
        @Override
        public AuthProvider create(Throwable throwable) {

            return new AuthProvider() {
                /**
                 * 降级统一返回无权限
                 *
                 * @param authentication
                 * @param url
                 * @param method
                 * @return <pre>
                 * Result:
                 * {
                 *   code:"-1"
                 *   mesg:"系统异常"
                 * }
                 * </pre>
                 */
                @Override
                public Result auth(String authentication, String url, String method) {
                    throwable.printStackTrace();
                    return Result.fail(PermissionErrorType.TOKEN_INVALID, throwable.getLocalizedMessage());
                }

                /**
                 * 降级统一返回无权限
                 *
                 * @param url
                 * @param method
                 * @return <pre>
                 * Result:
                 * {
                 *   code:"-1"
                 *   mesg:"系统异常"
                 * }
                 * </pre>
                 */
                @Override
                public Result ignoreAuthentication(String url, String method) {
                    throwable.printStackTrace();
                    return Result.fail(PermissionErrorType.TOKEN_INVALID, throwable.getLocalizedMessage());
                }
            };

        }
    }
}