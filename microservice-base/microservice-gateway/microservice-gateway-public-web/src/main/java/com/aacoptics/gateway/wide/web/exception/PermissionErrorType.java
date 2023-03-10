package com.aacoptics.gateway.wide.web.exception;

import com.aacoptics.common.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum PermissionErrorType implements ErrorType {

    USER_NOT_FOUND("030100", "用户未找到！"),
    ROLE_NOT_FOUND("030200", "角色未找到！"),
    TOKEN_EXPIRE("030300", "认证已过期！"),
    TOKEN_NOT_FOUND("030400", "认证为空！"),
    PERMS_SERVER_ERROR("030600", "调用鉴权接口发生错误！"),
    TOKEN_INVALID("030500", "Token非法无效！");

    /**
     * 错误类型码
     */
    private final String code;
    /**
     * 错误类型描述信息
     */
    private final String msg;

    PermissionErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
