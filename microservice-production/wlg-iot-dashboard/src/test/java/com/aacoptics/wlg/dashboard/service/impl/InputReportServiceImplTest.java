package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.InputReport;
import com.aacoptics.wlg.dashboard.service.InputReportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class InputReportServiceImplTest {

    @Resource
    InputReportService inputReportService;

    @Test
    public void getBydateAndMachineName() {

        InputReport inputReport = new InputReport();
        List<String> machineNames = new ArrayList<>();
        machineNames.add("HPM-008");
        machineNames.add("HPM-010");
        machineNames.add("HPM-0116");

        inputReport.setStartTime(LocalDate.now().atStartOfDay());
        inputReport.setEndTime(LocalDate.now().plusDays(1).atStartOfDay());
        inputReport.setMachineNames(machineNames);
        List<InputReport> res = inputReportService.getByDateAndMachineName(inputReport);

        List<InputReport> res1 = res;
    }
}