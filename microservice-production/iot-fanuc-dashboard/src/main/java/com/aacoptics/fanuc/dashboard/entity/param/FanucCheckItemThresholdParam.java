package com.aacoptics.fanuc.dashboard.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.fanuc.dashboard.entity.po.FanucCheckItemThreshold;
import lombok.Data;

@Data
public class FanucCheckItemThresholdParam extends BaseParam<FanucCheckItemThreshold> {

    private String machineName;


    private String moldFileName;


    private String checkItemName;


}
