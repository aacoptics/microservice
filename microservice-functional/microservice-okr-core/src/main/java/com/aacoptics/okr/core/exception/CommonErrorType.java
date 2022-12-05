package com.aacoptics.okr.core.exception;

import com.aacoptics.common.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum CommonErrorType implements ErrorType {
    BUSINESS_EXCEPTION("100002", "业务异常"),
    BUSINESS_DELETE_EXCEPTION("100003", "删除异常"),
    BUSINESS_ADD_EXCEPTION("100004", "添加异常"),
    SYNC_EXCEPTION("100005", "同步失败"),
    ALIGN_CYCLE_EXCEPTION("100006", "对齐目标不能选择已有关联关系的目标！");

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
