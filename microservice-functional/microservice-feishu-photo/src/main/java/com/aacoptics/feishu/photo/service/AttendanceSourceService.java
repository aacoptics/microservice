package com.aacoptics.feishu.photo.service;


import com.aacoptics.feishu.photo.entity.po.AttendanceSource;

public interface AttendanceSourceService {

    boolean saveAttendanceRecord(AttendanceSource attendanceSource, String locationName);

}
