package com.aacoptics.organization.exception;

import com.aacoptics.common.core.exception.BaseException;
import com.aacoptics.common.core.exception.ErrorType;

public class AuthenticationException extends BaseException {
    public AuthenticationException(ErrorType errorType) {
        super(errorType, errorType.getMsg());
    }
}
