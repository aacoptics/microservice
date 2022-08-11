package com.aacoptics.mobile.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.mobile.attendance.entity.AttendanceRecord;
import com.aacoptics.mobile.attendance.service.AttendanceService;
import com.aacoptics.mobile.attendance.util.Decrypt;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Value("${feishu.encryptKey}")
    private String encryptKey;

    @Override
    public void uploadAttendanceInfo(String encrypt) throws Exception {
        Decrypt d = new Decrypt(encryptKey);
        AttendanceRecord attendanceRecord = JSONObject.parseObject(d.decrypt(encrypt), AttendanceRecord.class);
    }
}
