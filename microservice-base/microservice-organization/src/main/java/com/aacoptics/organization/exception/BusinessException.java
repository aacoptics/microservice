package com.aacoptics.organization.exception;

import com.aacoptics.common.core.exception.BaseException;
import com.aacoptics.common.core.exception.ErrorType;

public class BusinessException extends BaseException {
    public BusinessException(ErrorType errorType) {
        super(errorType, errorType.getMsg());
    }

    public BusinessException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
