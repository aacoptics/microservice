package com.aacoptics.feishu.photo.service;


import com.aacoptics.feishu.photo.entity.vo.AttendanceRecord;

public interface AttendanceService {

    boolean uploadAttendanceInfo(AttendanceRecord attendanceRecord);
}
