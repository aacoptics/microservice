package com.aacoptics.wlg.dashboard.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoldingMachineStatusVO {

    private String equipName;

    private String project;

    private String moldName;

    private Long totalOutput;

    private Long output;

    private Long cycleTime;

    private String equipStatus;

    private Long statusDuration;

    private List<String> alarmInfo;

    private String oee;


}
