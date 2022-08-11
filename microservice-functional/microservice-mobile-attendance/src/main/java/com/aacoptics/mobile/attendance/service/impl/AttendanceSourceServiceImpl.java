package com.aacoptics.mobile.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.mobile.attendance.entity.po.AttendanceSource;
import com.aacoptics.mobile.attendance.entity.po.AttendanceSourceFeishu;
import com.aacoptics.mobile.attendance.mapper.AttendanceSourceMapper;
import com.aacoptics.mobile.attendance.service.AttendanceSourceFeishuService;
import com.aacoptics.mobile.attendance.service.AttendanceSourceService;
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
                boolean res = attendanceSourceFeishuService.saveAttendanceRecord(attendanceSourceFeishu);
                log.info(StrUtil.format("工号：{}，打卡时间：{}，同步卡机平台是否成功：{}"
                        , attendanceSource.getCardNo()
                        , attendanceSource.getFDateTime().toString()
                        , res));
                return res;
            } else return false;
        }
    }
}