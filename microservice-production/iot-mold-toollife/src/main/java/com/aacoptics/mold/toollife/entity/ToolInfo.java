package com.aacoptics.mold.toollife.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("tb_mold_program_sheet")
public class ToolInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String workpiece;

    private String monitorNo;

    private String material;

    private String route;

    private String programName;

    private String toolName;

    private String toolDiameter;

    private String toolNo;

    private String type;

    private String margin;

    private String toolValidLength;

    private String brand;

    private String maxDepth;

    private String workTime;

    private String cutDepth;

    private String feed;

    private String remark;

    private String machineNo;

    private LocalDateTime createDateTime;

    private String matHandleCode;

    private String matToolCode;

    private String matCode;

    private String matName;

    private String speed;

    private String lifeSalvage;

    private LocalDateTime maintenanceDateTime;

    @TableField(exist = false)
    private String actualLife;

    @TableField(exist = false)
    private String leftLife;

    @TableField(exist = false)
    private MatInfo matInfo;

    public void SetMoldMatInfo(){
        this.matInfo = new MatInfo();
        this.matInfo.setHandleCode(this.matHandleCode)
                .setToolCode(this.matToolCode)
                .setMatCode(this.matCode)
                .setMatName(this.matName)
                .setLifeSalvage(this.lifeSalvage)
                .setActualLife(this.actualLife)
                .setLeftLife(this.leftLife);
    }


}