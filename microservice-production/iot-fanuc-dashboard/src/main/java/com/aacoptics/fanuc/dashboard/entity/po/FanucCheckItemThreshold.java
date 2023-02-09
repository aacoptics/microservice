package com.aacoptics.fanuc.dashboard.entity.po;

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
@TableName("z_fanuc_check_item_threshold")
public class FanucCheckItemThreshold extends BasePo {

    /**
     * 站点
     */
    @TableField(value = "site")
    private String site;

    /**
     * 机台号
     */
    @TableField(value = "machine_name")
    private String machineName;


    /**
     * 项目号
     */
    @TableField(value = "mold_file_name")
    private String moldFileName;



    /**
     * 检查项
     */
    @TableField(value = "check_item")
    private String checkItem;


    /**
     * 检查项名称
     */
    @TableField(value = "check_item_name")
    private String checkItemName;

    /**
     * 偏移值
     */
    @TableField(value = "offset_value")
    private Double offsetValue;


}
