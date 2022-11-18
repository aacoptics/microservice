package com.aacoptics.wlg.dashboard.entity.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_wlg_molding_machine_cycle_detail")
public class CycleDetail implements Serializable {
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private long id;

    @Excel(name = "机台号", orderNum = "1")
    private String machineName;

    @Excel(name = "日期", orderNum = "2")
    private String cycleDate;

    @Excel(name = "开始时间", orderNum = "3")
    private String cycleTime;

    @ExcelIgnore
    private LocalDateTime startTime;

    @Excel(name = "班别", orderNum = "4")
    private String shiftClass;

    @Excel(name = "模次号", orderNum = "5")
    private String cycleNo;

    @Excel(name = "log file编号", orderNum = "6")
    private String waferId;

    @Excel(name = "Recipe name", orderNum = "7")
    private String recipeName;

    @Excel(name = "送测记录", orderNum = "8")
    private String measRecord;

    @Excel(name = "调整动作", orderNum = "9")
    private String operateRecord;

    @Excel(name = "是否粘下模", orderNum = "10")
    private String stickingLower;

    @Excel(name = "开模碎裂", orderNum = "11")
    private String dieBreaking;

    @Excel(name = "抓取碎裂", orderNum = "12")
    private String grabBreaking;

    @Excel(name = "冷却过程碎裂", orderNum = "13")
    private String coolingBreaking;

    @Excel(name = "备注", orderNum = "14")
    private String remark;

    @Excel(name = "性能确认-偏心", orderNum = "15")
    private String decenterResult;

    @Excel(name = "性能确认-芯厚", orderNum = "16")
    private String thicknessResult;

    @Excel(name = "性能确认-面型", orderNum = "17")
    private String mxpvResult;

    @Excel(name = "性能确认结果", orderNum = "18")
    private String performanceResult;

    @Excel(name = "综合结果", orderNum = "19")
    private String aggregateResult;

    @Excel(name = "批次号", orderNum = "20")
    private String batchNo;

    @Excel(name = "熔炼批次", orderNum = "21")
    private String smeltingBatch;

    @Excel(name = "U编号", orderNum = "22")
    private String uCode;

    @Excel(name = "U数值", orderNum = "23")
    private String uValue;

    @Excel(name = "U角度", orderNum = "24")
    private String uAngle;

    @Excel(name = "V编号", orderNum = "25")
    private String vCode;

    @Excel(name = "V数值", orderNum = "26")
    private String vValue;

    @Excel(name = "V角度", orderNum = "27")
    private String vAngle;

    @Excel(name = "W编号", orderNum = "28")
    private String wCode;

    @Excel(name = "W数值", orderNum = "29")
    private String wValue;

    @Excel(name = "W角度", orderNum = "30")
    private String wAngle;

    @Excel(name = "更新时间", orderNum = "31", format = "yyyy/MM/dd hh:mm:ss", width = 18)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Excel(name = "更新人", orderNum = "32")
    private String updateUser;

    @Excel(name = "创建时间", orderNum = "33", format = "yyyy/MM/dd hh:mm:ss", width = 18)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createTime;
}