package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.InputReport;
import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.aacoptics.wlg.dashboard.service.InputReportService;
import com.aacoptics.wlg.dashboard.service.MoldingMachineParamDataService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class InputReportServiceImplTest {

    @Resource
    InputReportService inputReportService;

    @Resource
    MoldingMachineParamDataService moldingMachineParamDataService;

    @Test
    public void getBydateAndMachineName() {

        List<MoldingMachineParamData> test = moldingMachineParamDataService.getMoldingParamData("HPM-013", "lower_moldcore_section_dutycycle_actual_0", Arrays.asList(
                "3968"));

        String sdf = "";
//        try {
//            inputReportService.SendPicNotification();
//        }
//        catch(IOException | ApiException err){
//            log.error(err.getMessage());
//        }

//        InputReport inputReport = new InputReport();
//        List<String> machineNames = new ArrayList<>();
//        machineNames.add("HPM-008");
//        machineNames.add("HPM-010");
//        machineNames.add("HPM-0116");
//
//        inputReport.setStartTime(LocalDate.now().atStartOfDay());
//        inputReport.setEndTime(LocalDate.now().plusDays(1).atStartOfDay());
//        inputReport.setMachineNames(machineNames);
//        List<InputReport> res = inputReportService.getByDateAndMachineName(inputReport);
//
//        List<InputReport> res1 = res;
    }
}