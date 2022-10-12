package com.aacoptics.wlg.equipment.exception;

import com.aacoptics.common.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum CommonErrorType implements ErrorType {

    Gaia_REST_INTERFACE_ERROR("100001", "GAIA接口异常！"),
    BUSINESS_EXCEPTION("100002", "业务异常"),
    BUSINESS_DELETE_EXCEPTION("100003", "删除异常"),
    BUSINESS_ADD_EXCEPTION("100004", "添加异常"),
    SYNC_EXCEPTION("100005", "同步失败");

    /**
     * 错误类型码
     */
    private final String code;
    /**
     * 错误类型描述信息
     */
    private final String msg;

    CommonErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
