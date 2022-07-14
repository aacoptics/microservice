package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.InputReport;
import com.aacoptics.wlg.dashboard.entity.MoldingMachineParamData;
import com.alibaba.fastjson.JSONArray;
import com.taobao.api.ApiException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface InputReportService {
    List<InputReport> getByDateAndMachineName(InputReport inputReport);

    void updateOutPutInfo(InputReport inputReport);

    void SendPicNotification() throws IOException, ApiException;
}
