package com.aacoptics.wlg.dashboard.exception;


import com.aacoptics.common.core.exception.BaseException;
import com.aacoptics.common.core.exception.ErrorType;

@SuppressWarnings("unused")
public class BusinessException extends BaseException {

    public BusinessException() {
        super(CommonErrorType.BUSINESS_EXCEPTION);
    }

    public BusinessException(String message) {
        super(CommonErrorType.BUSINESS_EXCEPTION, message);
    }

    public BusinessException(ErrorType errorType) {
        super(errorType, errorType.getMsg());
    }

    public BusinessException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
