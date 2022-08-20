package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.MoldingEventData;
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

    public IPage<MoldingEventData> getMachineEvents(String machineName,
                                                    LocalDateTime startTime,
                                                    LocalDateTime endTime,
                                                    Page page) {
        return moldingEventDataMapper.getMachineEvents(page, machineName, startTime, endTime);
    }

}