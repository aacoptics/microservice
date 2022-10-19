package com.aacoptics.hikvision.api.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aacoptics.hikvision.api.entity.param.DoorEventParam;
import com.aacoptics.hikvision.api.entity.po.HikvisionDoorEventRecord;
import com.aacoptics.hikvision.api.entity.vo.DoorEventDetail;
import com.aacoptics.hikvision.api.entity.vo.HikvisionApiPage;
import com.aacoptics.hikvision.api.mapper.HikvisionDoorEventRecordMapper;
import com.aacoptics.hikvision.api.service.HikvisionApiService;
import com.aacoptics.hikvision.api.service.HikvisionDoorEventRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class HikvisionDoorEventRecordServiceImpl extends ServiceImpl<HikvisionDoorEventRecordMapper, HikvisionDoorEventRecord> implements HikvisionDoorEventRecordService {


    @Resource
    HikvisionApiService hikvisionApiService;

    public void addBatch(List<HikvisionDoorEventRecord> records) {
        this.saveBatch(records);
    }

    @Override
    public void getHikvisionDoorEventRecord() {
        int pageIdx = 1;
        int totalPage = 1;
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN.withHour(LocalDateTime.now().minusHours(1).getHour()));
        LocalDateTime startTime = endTime.minusDays(1);
        while (pageIdx <= totalPage) {
            DoorEventParam doorEventParam = new DoorEventParam(startTime, endTime, pageIdx, 1000);
            HikvisionApiPage<DoorEventDetail> res = hikvisionApiService.getDoorEvents(doorEventParam);
            List<DoorEventDetail> doorEventDetails = res.getList();
            if (doorEventDetails.size() > 0) {
                List<HikvisionDoorEventRecord> records = new ArrayList<>();
                for (DoorEventDetail doorEventDetail : doorEventDetails) {
                    if (ObjectUtil.isEmpty(doorEventDetail.getPersonDetail()))
                        continue;
                    HikvisionDoorEventRecord hikvisionDoorEventRecord = new HikvisionDoorEventRecord();
                    hikvisionDoorEventRecord.setDoorName(doorEventDetail.getDoorName());
                    if (doorEventDetail.getDoorName().contains("进"))
                        hikvisionDoorEventRecord.setInAndOutType(1);
                    else if (doorEventDetail.getDoorName().contains("出"))
                        hikvisionDoorEventRecord.setInAndOutType(0);
                    else
                        hikvisionDoorEventRecord.setInAndOutType(-1);
                    hikvisionDoorEventRecord.setEventTime(doorEventDetail.getEventTime());
                    hikvisionDoorEventRecord.setJobNo(doorEventDetail.getPersonDetail().getJobNo());
                    hikvisionDoorEventRecord.setPersonName(doorEventDetail.getPersonDetail().getPersonName());
                    records.add(hikvisionDoorEventRecord);
                }
                addBatch(records);
            }
            totalPage = res.getTotalPage();
            pageIdx++;
        }
    }
}
