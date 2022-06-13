package com.aacoptics.notification.entity.param;

import com.aacoptics.notification.entity.po.XxlJobInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XxlJobInfoQueryParam extends XxlJobInfo {
    private String jobDesc;
}
