package com.aacoptics.mold.toollife.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("Sys_EquipInfo")
public class EquipInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("F_EquipName")
    private String fEquipName;

//    @TableField("F_EquipIp")
//    private String fEquipIp;
//
//    @TableField("F_EquipPort")
//    private String fEquipPort;
//
//    @TableField("F_ListenIp")
//    private String fListenIp;
//
//    @TableField("F_EquipType")
//    private String fEquipType;
//
//    @TableField("F_EquipVer")
//    private String fEquipVer;
//
//    @TableField("F_Project")
//    private String fProject;
//
//    @TableField("F_Process")
//    private String fProcess;
//
//    @TableField("F_Site")
//    private String fSite;
//
//    @TableField("F_Building")
//    private String fBuilding;
//
//    @TableField("F_Floor")
//    private String fFloor;
//
//    @TableField("F_Scence")
//    private String fScence;
//
//    @TableField("F_ToolBom")
//    private String fToolBom;
//
//    @TableField("F_Enable")
//    private Boolean fEnable;
//
//    @TableField("F_RowPosition")
//    private String fRowPosition;
//
//    @TableField("F_ColumnPosition")
//    private String fColumnPosition;
//
//    @TableField("F_Manager")
//    private String fManager;
//
//    @TableField("F_Cm")
//    private String fCm;
//
//    @TableField("F_Auto")
//    private Boolean fAuto;
//
//    @TableField("F_MarcoPj")
//    private String fMarcoPj;
//
//    @TableField("F_MarcoPc")
//    private String fMarcoPc;


}
