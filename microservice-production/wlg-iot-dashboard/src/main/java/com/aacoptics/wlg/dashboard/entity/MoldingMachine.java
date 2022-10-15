package com.aacoptics.wlg.dashboard.entity;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_opc_ua_connections")
public class MoldingMachine implements Serializable {

    private Integer id;
    private String ipAddress;
    private Integer port;
    private String machineName;
    private boolean isDelete;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String createdBy;
    private String updatedBy;
    private Integer namespaceIndex;
    private boolean feedingAlarm;
    private float standardCt;
}