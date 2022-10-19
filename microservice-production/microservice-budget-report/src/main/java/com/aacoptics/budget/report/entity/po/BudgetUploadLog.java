package com.aacoptics.budget.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("fb_budget_upload_log")
public class BudgetUploadLog extends BasePo {

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;


    /**
     * 文件名称
     */
    @TableField(value = "excel_name")
    private String excelName;


    /**
     * 上传时间
     */
    @TableField(value = "upload_time")
    private LocalDateTime uploadTime;


    /**
     * 上传用户
     */
    @TableField(value = "upload_user")
    private String uploadUser;

    /**
     * 生效状态，1-生效中， 0-已失效
     */
    @TableField(value = "status")
    private Integer status;

}
