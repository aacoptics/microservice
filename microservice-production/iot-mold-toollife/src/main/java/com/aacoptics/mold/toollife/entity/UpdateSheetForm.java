package com.aacoptics.mold.toollife.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class UpdateSheetForm implements Serializable {

    private List<ToolInfo> toolInfos;

    private String machineNo;
}
