package com.aacoptics.fanuc.dashboard.exception;

import com.aacoptics.common.core.exception.BaseException;

public class MachineNotFoundException extends BaseException {
    public MachineNotFoundException() {
        super(FanucDashboardErrorType.MACHINE_NOT_FOUND);
    }

    public MachineNotFoundException(String message) {
        super(FanucDashboardErrorType.MACHINE_NOT_FOUND, message);
    }
}
