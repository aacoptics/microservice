package com.aacoptics.wlg.dashboard.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_wlg_molding_machine_abnormal_data")
public class MoldingAbnormalData implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String machineName;

    private String waferId;

    private String materialName;

    private String projectName;

    private String modelName;

    private String abnormalParamName;

    private String abnormalType;

    private String avgValue;

    private String abnormalValue;

    private LocalDateTime createTime;
}