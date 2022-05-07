package com.aacoptics.organization.exception;

import com.aacoptics.common.core.exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(OrganizationErrorType.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(OrganizationErrorType.USER_NOT_FOUND, message);
    }
}
