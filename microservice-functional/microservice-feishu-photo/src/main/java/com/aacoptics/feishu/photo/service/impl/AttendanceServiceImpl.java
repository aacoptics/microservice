package com.aacoptics.feishu.photo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.feishu.photo.entity.po.AttendanceSource;
import com.aacoptics.feishu.photo.entity.vo.AttendanceRecord;
import com.aacoptics.feishu.photo.service.AttendanceService;
import com.aacoptics.feishu.photo.service.AttendanceSourceService;
import com.aacoptics.feishu.photo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Resource
    private EmployeeService employeeService;

    @Resource
    private AttendanceSourceService attendanceSourceService;

    @Override
    public boolean uploadAttendanceInfo(AttendanceRecord attendanceRecord) {
        String jobNumber = attendanceRecord.getEvent().getEmployee_no();
        if (!StrUtil.isBlank(jobNumber)) {
            boolean isOpticsEmployee = employeeService.checkEmployeeExist(jobNumber);
            if (isOpticsEmployee) {
                LocalDateTime fDateTime = LocalDateTime.ofEpochSecond(attendanceRecord.getEvent().getCheck_time(), 0, ZoneOffset.ofHours(8));
                AttendanceSource attendanceSource = new AttendanceSource();
                attendanceSource.setCardNo(jobNumber);
                attendanceSource.setMachId(72);
                attendanceSource.setFDateTime(fDateTime);
                return attendanceSourceService.saveAttendanceRecord(attendanceSource, attendanceRecord.getEvent().getLocation_name());
            } else return true;
        }
        return false;
    }
}