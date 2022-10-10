package com.aacoptics.wlg.dashboard.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
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
@TableName("t_wlg_molding_machine_input_per_six_hour")
public class InputReport implements Serializable {

    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    @Excel(name = "机台号", orderNum = "3")
    private String machineName;

    @Excel(name = "材料号", orderNum = "4")
    private String materialName;

    @Excel(name = "项目", orderNum = "5")
    private String projectName;

    @Excel(name = "模具", orderNum = "6")
    private String modelName;

    @Excel(name = "周期", orderNum = "7")
    private String cycleName;

    @Excel(name = "阶段", orderNum = "8")
    private String periodName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", orderNum = "1", format = "hh:mm", width = 6)
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", orderNum = "2", format = "hh:mm", width = 6)
    private LocalDateTime endTime;

    @ExcelIgnore
    private Integer avgCycle;

    @Excel(name = "投入数", orderNum = "11")
    private Integer inputQty;

    @Excel(name = "起始模次", orderNum = "9")
    private String startWaferId;

    @Excel(name = "截止模次", orderNum = "10")
    private String endWaferId;

    @Excel(name = "碎裂可流转", orderNum = "12")
    private Integer brokenOk;

    @Excel(name = "碎裂不可流转", orderNum = "13")
    private Integer brokenNg;

    @Excel(name = "产出数", orderNum = "14")
    private Integer outputQty;

    @Excel(name = "日期", orderNum = "15", format = "yyyy/MM/dd", width = 12)
    private LocalDateTime createTime;

    @ExcelIgnore
    private LocalDateTime updateTime;

    @ExcelIgnore
    private String updateUser;

    @ExcelIgnore
    private String abnormalReason;

    @TableField(exist = false)
    private List<String> machineNames;

    @TableField(exist = false)
    @ExcelIgnore
    private float standardCt;
}