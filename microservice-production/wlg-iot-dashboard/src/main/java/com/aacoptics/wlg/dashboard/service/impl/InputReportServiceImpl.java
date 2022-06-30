package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.InputReport;
import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.aacoptics.wlg.dashboard.mapper.InputReportMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineParamDataMapper;
import com.aacoptics.wlg.dashboard.service.InputReportService;
import com.aacoptics.wlg.dashboard.service.MoldingMachineParamDataService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class InputReportServiceImpl extends ServiceImpl<InputReportMapper, InputReport> implements InputReportService {

    @Resource
    InputReportMapper inputReportMapper;

    @Override
    public List<InputReport> getByDateAndMachineName(InputReport inputReport) {
        return inputReportMapper.getByDateAndMachineName(inputReport.getStartTime(), inputReport.getEndTime(), inputReport.getMachineNames());
    }

    @Override
    public void updateOutPutInfo(InputReport inputReport) {
            UpdateWrapper<InputReport> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("broken_ok", inputReport.getBrokenOk());
            updateWrapper.set("broken_ng", inputReport.getBrokenNg());
            updateWrapper.set("output_qty", inputReport.getOutputQty());
            updateWrapper.eq("id", inputReport.getId());
            this.update(updateWrapper);
    }
}