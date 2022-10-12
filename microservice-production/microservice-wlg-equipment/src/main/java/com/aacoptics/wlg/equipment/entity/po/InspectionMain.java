package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_inspection_main")
public class InspectionMain extends BasePo {

    /**
     * 资产名称
     */
    @TableField(value = "mch_name")
    private String mchName;

    /**
     * 规格
     */
    @TableField(value = "spec")
    private String spec;

    /**
     * 型号
     */
    @TableField(value = "type_version")
    private String typeVersion;

    /**
     * 点检项
     */
    @TableField(exist = false)
    private List<InspectionItem> inspectionItemList;

    /**
     * 点检班次
     */
    @TableField(exist = false)
    private List<InspectionShift> inspectionShiftList;

}
