package com.aacoptics.fanuc.dashboard.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FanucDigitalData  implements Serializable {
    private String monitMcName;

    private String monitStatus;

    private String condMoldFileName;

    private String monitDateTime;

    private String monitCycleCount;

    private String monitShotCount;

    private String monitGoodCount;

    private String monitCycle;

    private String moldAutomate;

    private String moldWait;

    private String moldManual;

    private String moldAlarm;

    private String moldComplete;

    private String moldShutdown;

    private String moldTotal;

    private String moldEnergyPerGoodShot;

    private String moldEnergy;

    private List<FanucOneHourShotCountData> uph;
}
