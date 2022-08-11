package com.aacoptics.mobile.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.mobile.attendance.config.FeishuAppKeyConfig;
import com.aacoptics.mobile.attendance.entity.po.AttendanceSource;
import com.aacoptics.mobile.attendance.entity.vo.AttendanceRecord;
import com.aacoptics.mobile.attendance.service.AttendanceService;
import com.aacoptics.mobile.attendance.service.AttendanceSourceService;
import com.aacoptics.mobile.attendance.service.EmployeeService;
import com.aacoptics.mobile.attendance.util.Decrypt;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Resource
    private FeishuAppKeyConfig feishuAppKeyConfig;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private AttendanceSourceService attendanceSourceService;

    @Override
    public boolean uploadAttendanceInfo(String encrypt) throws Exception {
        Decrypt d = new Decrypt(feishuAppKeyConfig.getEncryptKey());
        AttendanceRecord attendanceRecord = JSONObject.parseObject(d.decrypt(encrypt), AttendanceRecord.class);
        String jobNumber = attendanceRecord.getEvent().getEmployee_no();
        if (!StrUtil.isBlank(jobNumber)) {
            boolean isOpticsEmployee = employeeService.checkEmployeeExist(jobNumber);
            if (isOpticsEmployee) {
                LocalDateTime fDateTime = LocalDateTime.ofEpochSecond(attendanceRecord.getEvent().getCheck_time(), 0, ZoneOffset.ofHours(8));
                AttendanceSource attendanceSource = new AttendanceSource();
                attendanceSource.setCardNo(jobNumber);
                attendanceSource.setMachId(9999);
                attendanceSource.setFDateTime(fDateTime);
                return attendanceSourceService.saveAttendanceRecord(attendanceSource, attendanceRecord.getEvent().getLocation_name());
            } else return true;
        }
        return false;
    }
}