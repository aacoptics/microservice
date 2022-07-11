package com.aacoptics.mold.toollife.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class ToolMachineNo implements Serializable {
    private Integer id;

    private String machineNo;
}
