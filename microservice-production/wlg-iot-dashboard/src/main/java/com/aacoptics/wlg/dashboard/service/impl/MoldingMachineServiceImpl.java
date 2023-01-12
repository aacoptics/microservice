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
import java.util.*;
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
        queryWrapper.orderByDesc("start_time");

        List<InputReport> inputReportList = inputReportMapper.selectList(queryWrapper);
        if(inputReportList != null && inputReportList.size() > 0)
        {
            InputReport inputReport = inputReportList.get(0);
            moldingMachineStatusVO.setProject(inputReport.getProjectName());
            moldingMachineStatusVO.setMoldName(inputReport.getModelName());


            Long outputQty = inputReportList.stream().mapToLong(InputReport::getOutputQty).sum();
            Long cycleTime = inputReportList.stream().mapToLong(InputReport::getAvgCycle).sum() / inputReportList.size();

            moldingMachineStatusVO.setOutput(outputQty);
            moldingMachineStatusVO.setCycleTime(cycleTime);

            for(int i=0; i<inputReportList.size(); i++) {
                if(inputReportList.get(i).getEndWaferId() != null) {
                    Long totalOutput = Long.valueOf(inputReportList.get(i).getEndWaferId().substring(1));
                    moldingMachineStatusVO.setTotalOutput(totalOutput);
                    break;
                }
            }
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

        DecimalFormat decimalFormat = new DecimalFormat("0.##%");
        String oeePercent = decimalFormat.format(oee);
        moldingMachineStatusVO.setOee(oeePercent);

        return moldingMachineStatusVO;
    }


    @Override
    public MoldingMachineStatusSummaryVO getMachineStatusSummaryInfo() {
        MoldingMachineStatusSummaryVO moldingMachineStatusSummaryVO = new MoldingMachineStatusSummaryVO();

        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        List<Map<String, Object>> inputReportList = inputReportMapper.getMachineOutputSummary(startTime);
        moldingMachineStatusSummaryVO.setCurrentOutput(inputReportList);

        //获取设备状态统计
        List<Map<String, Object>> machineStatusSummaryList = moldingMachineStatusDataMapper.getMachineStatusSummary();

        HashMap<String, Integer> equipStatusMap = new HashMap<>();
        if(machineStatusSummaryList != null && machineStatusSummaryList.size() > 0)
        {
            for(Map<String, Object> map : machineStatusSummaryList)
            {
                equipStatusMap.put(map.get("status_value")+"", Integer.valueOf(map.get("status_count")+""));
            }
        }
        moldingMachineStatusSummaryVO.setEquipStatus(equipStatusMap);

        //温湿度取随机值
        //温度20到25
        //湿度40到50
        HashMap<String, Integer> environmentMap = new HashMap<>();
        environmentMap.put("temperature", this.getRandomNumberInRange(20, 25));
        environmentMap.put("humidity", this.getRandomNumberInRange(40, 50));
        moldingMachineStatusSummaryVO.setEnvironment(environmentMap);

        DecimalFormat decimalFormat = new DecimalFormat("0.##%");

        //Yield rate
        Map<String, Object> totalQtyMap = inputReportMapper.getMoldingMachineTotalQty(startTime);
        BigDecimal inputTotalQty = BigDecimal.valueOf(Long.parseLong(totalQtyMap.get("total_input_qty")+""));
        BigDecimal outputTotalQty = BigDecimal.valueOf(Long.parseLong(totalQtyMap.get("total_output_qty")+""));

        inputTotalQty = inputTotalQty.equals(BigDecimal.ZERO) ? BigDecimal.ONE : inputTotalQty;
        BigDecimal yieldRate = outputTotalQty.divide(inputTotalQty,4, BigDecimal.ROUND_CEILING);
        String yieldRatePercent = decimalFormat.format(yieldRate);
        moldingMachineStatusSummaryVO.setYieldRate(yieldRatePercent);

        //OEE
        Long activatedTime = moldingMachineStatusDataMapper.getMachineActivatedTotalTime(startTime);
        Long machineCount = moldingMachineStatusDataMapper.getMachineActivatedCount(startTime);
        BigDecimal oee = BigDecimal.valueOf(activatedTime).divide(
                BigDecimal.valueOf(Duration.between(startTime, currentTime).toMillis()*machineCount/1000), 4, BigDecimal.ROUND_CEILING);


        String oeePercent = decimalFormat.format(oee);
        moldingMachineStatusSummaryVO.setOee(oeePercent);

        return moldingMachineStatusSummaryVO;
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}