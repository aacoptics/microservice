package com.aacoptics.mobile.attendance.service.impl;

import com.aacoptics.mobile.attendance.entity.po.AttendanceSource;
import com.aacoptics.mobile.attendance.mapper.AttendanceSourceMapper;
import com.aacoptics.mobile.attendance.service.AttendanceSourceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttendanceSourceServiceImpl extends ServiceImpl<AttendanceSourceMapper, AttendanceSource> implements AttendanceSourceService {

    @Override
    public boolean saveAttendanceRecord(AttendanceSource attendanceSource) {
        int count = this.count(new QueryWrapper<AttendanceSource>()
                .eq("CardNo", attendanceSource.getCardNo())
                .eq("MachID", attendanceSource.getMachId())
                .eq("FDateTime", attendanceSource.getFDateTime())
        );
        if (count > 0)
            return true;
        else
            return this.save(attendanceSource);
    }
}