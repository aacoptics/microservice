package com.aacoptics.wlg.dashboard.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoldingMachineStatusSummaryVO {

    private String yieldRate;

    private List<Map<String, Object>> currentOutput;

    private Map<String, Integer> equipStatus;

    private Map<String, Integer> environment;

    private String oee;


}
