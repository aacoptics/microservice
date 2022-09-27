package com.aacoptics.mobile.attendance.service;

import com.aacoptics.mobile.attendance.entity.vo.AttendanceRecord;

public interface AttendanceService {

    boolean uploadAttendanceInfo(AttendanceRecord attendanceRecord);
}
