package com.aacoptics.feishu.photo.service.impl;

import com.aacoptics.feishu.photo.entity.po.AttendanceSource;
import com.aacoptics.feishu.photo.entity.po.AttendanceSourceFeishu;
import com.aacoptics.feishu.photo.mapper.AttendanceSourceMapper;
import com.aacoptics.feishu.photo.service.AttendanceSourceFeishuService;
import com.aacoptics.feishu.photo.service.AttendanceSourceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class AttendanceSourceServiceImpl extends ServiceImpl<AttendanceSourceMapper, AttendanceSource> implements AttendanceSourceService {

    @Resource
    private AttendanceSourceFeishuService attendanceSourceFeishuService;

    @Override
    @Transactional
    public boolean saveAttendanceRecord(AttendanceSource attendanceSource, String locationName) {
        int count = this.count(new QueryWrapper<AttendanceSource>()
                .eq("CardNo", attendanceSource.getCardNo())
                .eq("MachID", attendanceSource.getMachId())
                .eq("FDateTime", attendanceSource.getFDateTime())
        );
        if (count > 0)
            return true;
        else {
            if (this.save(attendanceSource)) {
                AttendanceSourceFeishu attendanceSourceFeishu = new AttendanceSourceFeishu();
                attendanceSourceFeishu.setParentId(attendanceSource.getId());
                attendanceSourceFeishu.setLocationName(locationName);
                return attendanceSourceFeishuService.saveAttendanceRecord(attendanceSourceFeishu);
            } else return false;
        }
    }
}