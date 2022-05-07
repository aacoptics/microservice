package com.aacoptics.organization.exception;

import com.aacoptics.common.core.exception.BaseException;

public class ExistChildMenuException extends BaseException {
    public ExistChildMenuException() {
        super(OrganizationErrorType.EXIST_CHILD_MENU);
    }

    public ExistChildMenuException(String message) {
        super(OrganizationErrorType.EXIST_CHILD_MENU, message);
    }
}
