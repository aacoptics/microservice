package com.aacoptics.organization.exception;

import com.aacoptics.common.core.exception.BaseException;

public class DataNotFoundException extends BaseException {
    public DataNotFoundException() {
        super(OrganizationErrorType.DATA_NOT_FOUND);
    }

    public DataNotFoundException(String message) {
        super(OrganizationErrorType.DATA_NOT_FOUND, message);
    }
}
