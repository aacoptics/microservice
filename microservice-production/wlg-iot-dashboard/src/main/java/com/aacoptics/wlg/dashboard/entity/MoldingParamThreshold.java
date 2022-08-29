package com.aacoptics.wlg.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_wlg_molding_threshold_config")
public class MoldingParamThreshold implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer machineId;
    private Integer paramId;
    private Integer arrayId;
    private Double threshold;

    private Double maxValue;

    private Double minValue;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String createdBy;
    private String updatedBy;
}