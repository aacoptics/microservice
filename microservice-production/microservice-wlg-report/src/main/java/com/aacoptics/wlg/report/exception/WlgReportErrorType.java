package com.aacoptics.wlg.report.exception;

import com.aacoptics.common.core.exception.ErrorType;
import lombok.Getter;

@Getter
public enum WlgReportErrorType implements ErrorType {

    DATA_NOT_FOUND("050100", "数据未找到！"),
    BUSINESS_EXCEPTION("050200", "业务异常");

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    WlgReportErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
