package com.aacoptics.wlg.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_wlg_molding_machine_param_data")
public class InputReport implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String machineName;

    private String materialName;

    private String projectName;

    private String modelName;

    private String cycleName;

    private String periodName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Integer avgCycle;

    private Integer inputQty;

    private String startWaferId;

    private String endWaferId;

    private Integer brokenOk;

    private Integer brokenNg;

    private Integer outputQty;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<String> machineNames;
}