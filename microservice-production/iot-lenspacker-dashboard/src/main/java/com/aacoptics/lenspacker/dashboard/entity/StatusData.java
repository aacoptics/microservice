package com.aacoptics.lenspacker.dashboard.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StatusData implements Serializable {

    private Integer isComplete;

    private Float machineCT;

    private Float outputQty;

    private Integer cavityInfo;

    private Integer cavityNo;

    private Integer cavityNums;

    private String machineNo;

    private String site;

    private Integer status;

    private String alarmInfo;
}
