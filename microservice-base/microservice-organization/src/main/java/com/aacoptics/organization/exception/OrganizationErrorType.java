package com.aacoptics.organization.exception;

import com.aacoptics.common.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum OrganizationErrorType implements ErrorType {

    USER_NOT_FOUND("030100", "用户未找到！"),
    ROLE_NOT_FOUND("030200", "角色未找到！"),
    TOKEN_EXPIRE("030300", "Token已过期！"),
    TOKEN_NOT_FOUND("030400", "Token为空！"),
    TOKEN_INVALID("030500", "Token非法无效！"),
    UNIQUE_Exception("030600", "唯一性约束异常！"),
    DATA_NOT_FOUND("030700", "用户未找到！"),
    UPDATE_Exception("030800", "更新异常！"),
    DELETE_Exception("030900", "删除异常！"),
    EXIST_CHILD_MENU("031000", "存在子菜单，请先删除子菜单！");

    /**
     * 错误类型码
     */
    private final String code;
    /**
     * 错误类型描述信息
     */
    private final String msg;

    OrganizationErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
