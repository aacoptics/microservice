package com.aacoptics.budget.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("fb_product_line_permission")
public class ProductLinePermission extends BasePo {


    /**
     * 事业部
     */
    @TableField(value = "business_division")
    private String businessDivision;


    /**
     * 产品线
     */
    @TableField(value = "product_line")
    private String productLine;

    /**
     * 域账号
     */
    @TableField(value = "user_code")
    private String userCode;


}
