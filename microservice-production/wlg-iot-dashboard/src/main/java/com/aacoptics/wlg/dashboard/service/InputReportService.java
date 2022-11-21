package com.aacoptics.wlg.dashboard.service;

import com.aacoptics.wlg.dashboard.entity.po.InputReport;
import com.taobao.api.ApiException;

import java.io.IOException;
import java.util.List;

public interface InputReportService {
    List<InputReport> getByDateAndMachineName(InputReport inputReport);

    void updateOutPutInfo(InputReport inputReport);

    void SendPicNotification() throws IOException, ApiException;
}
