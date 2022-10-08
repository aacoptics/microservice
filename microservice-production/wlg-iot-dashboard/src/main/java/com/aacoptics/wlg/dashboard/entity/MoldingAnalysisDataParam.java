package com.aacoptics.wlg.dashboard.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MoldingAnalysisDataParam implements Serializable {

    private String machineName;
    private List<String> paramNames;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
