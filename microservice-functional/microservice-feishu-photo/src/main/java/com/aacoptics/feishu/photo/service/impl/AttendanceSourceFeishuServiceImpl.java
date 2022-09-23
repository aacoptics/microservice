package com.aacoptics.feishu.photo.service.impl;

import com.aacoptics.feishu.photo.entity.po.AttendanceSourceFeishu;
import com.aacoptics.feishu.photo.mapper.AttendanceSourceFeishuMapper;
import com.aacoptics.feishu.photo.service.AttendanceSourceFeishuService;
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