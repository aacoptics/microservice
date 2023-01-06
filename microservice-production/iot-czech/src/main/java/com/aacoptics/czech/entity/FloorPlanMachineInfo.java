package com.aacoptics.czech.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class FloorPlanMachineInfo implements Serializable {
    private String machineNo;

    private String mold;

    private String project;

    private String side;

    private String process;

    private String ML;

    private String hmiVersion;

    private String status;

    private String showStatus;

    private int showStatusCode;

    private String temperature;

    private String cavity;
}
