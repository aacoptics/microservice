package com.aacoptics.notification.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.notification.entity.po.Robot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotQueryParam extends BaseParam<Robot> {
    private String robotName;
    private String robotType;
}
