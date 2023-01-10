package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.wlg.dashboard.entity.po.InputReport;
import com.aacoptics.wlg.dashboard.entity.po.MoldingEventData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingMachine;
import com.aacoptics.wlg.dashboard.entity.po.MoldingMachineStatusData;
import com.aacoptics.wlg.dashboard.entity.vo.MoldingMachineStatusSummaryVO;
import com.aacoptics.wlg.dashboard.entity.vo.MoldingMachineStatusVO;
import com.aacoptics.wlg.dashboard.mapper.InputReportMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingEventDataMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineStatusDataMapper;
import com.aacoptics.wlg.dashboard.service.InputReportService;
import com.aacoptics.wlg.dashboard.service.MoldingMachineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class MoldingMachineServiceImpl extends ServiceImpl<MoldingMachineMapper, MoldingMachine> implements MoldingMachineService {

    @Autowired
    private InputReportMapper inputReportMapper;

    @Autowired
    private MoldingMachineStatusDataMapper moldingMachineStatusDataMapper;

    @Autowired
    private MoldingEventDataMapper moldingEventDataMapper;

    @Override
    public IPage<MoldingMachine> query(Page page, String machineName) {
        QueryWrapper<MoldingMachine> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(machineName), "machine_name", machineName);
        queryWrapper.orderByAsc("machine_name");
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean update(MoldingMachine moldingMachine) {
        UpdateWrapper<MoldingMachine> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("updated_time", LocalDateTime.now())
                .set("updated_by", UserContextHolder.getInstance().getUsername())
                .set("feeding_alarm", moldingMachine.isFeedingAlarm())
                .set("standard_ct", moldingMachine.getStandardCt())
                .eq("id", moldingMachine.getId());

        return this.update(updateWrapper);
    }

    @Override
    public MoldingMachineStatusVO getMachineStatusInfo(String equipName) {
        MoldingMachineStatusVO moldingMachineStatusVO = new MoldingMachineStatusVO();
        moldingMachineStatusVO.setEquipName(equipName);

        LocalDateTime todayStartTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime currentTime = LocalDateTime.now();

        QueryWrapper<InputReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("machine_name", equipName);
        queryWrapper.ge("start_time", todayStartTime);
        queryWrapper.orderByAsc("start_time");

        List<InputReport> inputReportList = inputReportMapper.selectList(queryWrapper);
        if(inputReportList != null && inputReportList.size() > 0)
        {
            InputReport inputReport = inputReportList.get(0);
            moldingMachineStatusVO.setProject(inputReport.getProjectName());
            moldingMachineStatusVO.setMoldName(inputReport.getModelName());

            List<Long> outputList = inputReportList.stream()
                    .map(tempInputReport ->
                    Long.valueOf(tempInputReport.getEndWaferId() != null ? tempInputReport.getEndWaferId().substring(1) : "0"))
                    .collect(Collectors.toList());

            Long outputQty = inputReportList.stream().mapToLong(InputReport::getOutputQty).sum();
            Long cycleTime = inputReportList.stream().mapToLong(InputReport::getAvgCycle).sum();

            moldingMachineStatusVO.setOutput(outputQty);
            moldingMachineStatusVO.setCycleTime(cycleTime);

            Long totalOutput = outputList.stream().mapToLong(Long::longValue).sum();
            moldingMachineStatusVO.setTotalOutput(totalOutput);
        }

        //获取机台状态
        MoldingMachineStatusData moldingMachineStatusData = moldingMachineStatusDataMapper.getTopOneMachineStatusData(equipName);
        if(moldingMachineStatusData != null) {
            moldingMachineStatusVO.setEquipStatus(moldingMachineStatusData.getStatusValue());

            Duration duration = Duration.between(moldingMachineStatusData.getStartTime(), currentTime);
            moldingMachineStatusVO.setStatusDuration(duration.toMillis()/1000);
        }
        //获取报警信息
        QueryWrapper<MoldingEventData> moldingEventDataQueryWrapper = new QueryWrapper<MoldingEventData>();
        moldingEventDataQueryWrapper.eq("machine_name", equipName);
        moldingEventDataQueryWrapper.ge("start_time", todayStartTime);
        moldingEventDataQueryWrapper.isNull("end_time");
        moldingEventDataQueryWrapper.orderByAsc("start_time");

        List<MoldingEventData> moldingEventDataList = moldingEventDataMapper.selectList(moldingEventDataQueryWrapper);
        if(moldingEventDataList != null && moldingEventDataList.size() > 0)
        {
            moldingMachineStatusVO.setAlarmInfo(moldingEventDataList.stream()
                    .map(moldingEventData -> moldingEventData.getAlarmInfo())
                    .collect(Collectors.toList()));
        }
        // ACTIVATED 总时长 / 当前时间减0点
        Long activatedTime = moldingMachineStatusDataMapper.getMachineActivatedTime(equipName, todayStartTime);
        BigDecimal oee = BigDecimal.valueOf(activatedTime).divide(
                BigDecimal.valueOf(Duration.between(todayStartTime, currentTime).toMillis()/1000), 4, BigDecimal.ROUND_CEILING);

        DecimalFormat decimalFormat = new DecimalFormat("0%");
        String oeePercent = decimalFormat.format(oee);
        moldingMachineStatusVO.setOee(oeePercent);

        return moldingMachineStatusVO;
    }


    @Override
    public MoldingMachineStatusSummaryVO getMachineStatusSummaryInfo(String summaryDate) {
        MoldingMachineStatusSummaryVO moldingMachineStatusSummaryVO = new MoldingMachineStatusSummaryVO();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime summaryDateTimeStart = LocalDateTime.parse(summaryDate + " 00:00:00", dateTimeFormatter);
        LocalDateTime summaryDateTimeEnd = LocalDateTime.parse(summaryDate + " 00:00:00", dateTimeFormatter).plusDays(1);

        List<Map<String, Object>> inputReportList = inputReportMapper.getMachineOutputSummary(summaryDateTimeStart, summaryDateTimeEnd);
        moldingMachineStatusSummaryVO.setCurrentOutput(inputReportList);

        //获取设备状态统计
        List<Map<String, Object>> machineStatusSummaryList = moldingMachineStatusDataMapper.getMachineStatusSummary(summaryDateTimeStart, summaryDateTimeEnd);

        List<Map<String, Object>> equipStatus = new ArrayList();
        if(machineStatusSummaryList != null && machineStatusSummaryList.size() > 0)
        {
            for(Map<String, Object> map : machineStatusSummaryList)
            {
                HashMap<String, Object> equipStatusMap = new HashMap<>();
                equipStatusMap.put(map.get("status_value")+"", Long.valueOf(map.get("status_count")+""));
                equipStatus.add(equipStatusMap);
            }
        }
        moldingMachineStatusSummaryVO.setEquipStatus(equipStatus);
// TODO
//        moldingMachineStatusSummaryVO.setEnvironment();
//        moldingMachineStatusSummaryVO.setYieldRate();
//        moldingMachineStatusSummaryVO.setOee();

        return moldingMachineStatusSummaryVO;
    }
}