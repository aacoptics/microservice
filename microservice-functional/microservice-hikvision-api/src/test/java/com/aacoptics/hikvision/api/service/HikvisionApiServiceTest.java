package com.aacoptics.hikvision.api.service;


import com.aacoptics.hikvision.api.entity.param.DoorEventParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class HikvisionApiServiceTest {

    @Resource
    HikvisionApiService hikvisionApiService;

    @Resource
    HikvisionDoorEventRecordService hikvisionDoorEventRecordService;

    @Test
    public void getCameraPreviewURL() {
        DoorEventParam doorEventParam = new DoorEventParam();
        doorEventParam.setStartTime(LocalDateTime.now().minusDays(1));
        doorEventParam.setEndTime(LocalDateTime.now());
        doorEventParam.setPageNo(1);
        hikvisionApiService.getDoorEvents(doorEventParam);
    }

    @Test
    public void getHikvisionDoorEventRecord(){
        hikvisionDoorEventRecordService.getHikvisionDoorEventRecord();
    }
}