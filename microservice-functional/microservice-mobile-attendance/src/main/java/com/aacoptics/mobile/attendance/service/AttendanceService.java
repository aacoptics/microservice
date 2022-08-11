package com.aacoptics.mobile.attendance.service;

import com.aacoptics.mobile.attendance.entity.AttendanceRecord;

public interface AttendanceService {

    void uploadAttendanceInfo(String encrypt) throws Exception;
}
