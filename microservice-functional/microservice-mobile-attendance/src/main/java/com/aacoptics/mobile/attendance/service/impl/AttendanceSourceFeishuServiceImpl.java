package com.aacoptics.mobile.attendance.service.impl;

import com.aacoptics.mobile.attendance.entity.po.AttendanceSourceFeishu;
import com.aacoptics.mobile.attendance.mapper.AttendanceSourceFeishuMapper;
import com.aacoptics.mobile.attendance.service.AttendanceSourceFeishuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttendanceSourceFeishuServiceImpl extends ServiceImpl<AttendanceSourceFeishuMapper, AttendanceSourceFeishu> implements AttendanceSourceFeishuService {

    @Override
    public boolean saveAttendanceRecord(AttendanceSourceFeishu attendanceSourceFeishu) {
        return this.save(attendanceSourceFeishu);
    }
}