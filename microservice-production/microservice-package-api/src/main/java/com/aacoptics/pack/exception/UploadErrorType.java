package com.aacoptics.pack.exception;

import com.aacoptics.common.core.exception.ErrorType;
import lombok.Getter;

@Getter
public class UploadErrorType implements ErrorType {

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    public UploadErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
