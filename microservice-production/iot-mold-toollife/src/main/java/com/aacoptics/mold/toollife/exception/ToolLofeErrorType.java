package com.aacoptics.mold.toollife.exception;

import com.aacoptics.common.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum ToolLofeErrorType implements ErrorType {

    MONITOR_IS_BLANK("050001", "监控号为空！");

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    ToolLofeErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
