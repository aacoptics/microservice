package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.po.MoldingAbnormalData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingEventData;
import com.aacoptics.wlg.dashboard.mapper.MoldingAbnormalDataMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingEventDataMapper;
import com.aacoptics.wlg.dashboard.service.MoldingEventDataService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MoldingEventDataServiceImpl extends ServiceImpl<MoldingEventDataMapper, MoldingEventData> implements MoldingEventDataService {
    @Resource
    private MoldingEventDataMapper moldingEventDataMapper;

    @Resource
    private MoldingAbnormalDataMapper moldingAbnormalDataMapper;

    public IPage<MoldingEventData> getMachineEvents(String machineName,
                                                    LocalDateTime startTime,
                                                    LocalDateTime endTime,
                                                    Page page) {
        return moldingEventDataMapper.getMachineEvents(page, machineName, startTime, endTime);
    }

    public IPage<MoldingEventData> getMachineErrors(String machineName,
                                                    LocalDateTime startTime,
                                                    LocalDateTime endTime,
                                                    Page page) {
        return moldingEventDataMapper.getMachineErrors(page, machineName, startTime, endTime);
    }

    public IPage<MoldingAbnormalData> getMachineAbnormalData(String machineName,
                                                      LocalDateTime startTime,
                                                      LocalDateTime endTime,
                                                      Page page){
        return moldingAbnormalDataMapper.getMachineAbnormalData(page, machineName, startTime, endTime);

    }

    public IPage<MoldingEventData> getMachineStatus(List<String> machineName,
                                                    LocalDateTime startTime,
                                                    LocalDateTime endTime,
                                                    Page page) {
        return moldingEventDataMapper.getMachineStatus(page, machineName, startTime, endTime);
    }


}