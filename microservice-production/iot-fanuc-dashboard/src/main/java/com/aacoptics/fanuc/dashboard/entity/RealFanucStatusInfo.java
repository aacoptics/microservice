package com.aacoptics.fanuc.dashboard.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RealFanucStatusInfo implements Serializable {
    private String monitMcName;

    private String monitStatus;

    private String condMoldFileName;

    private String monitDateTime;

    private String monitCycleCount;

    private String monitShotCount;

    private String monitGoodCount;

    private String monitCycle;

    private Object moldData;
}
