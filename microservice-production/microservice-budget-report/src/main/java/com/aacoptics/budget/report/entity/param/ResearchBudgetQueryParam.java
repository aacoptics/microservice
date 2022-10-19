package com.aacoptics.budget.report.entity.param;

import com.aacoptics.budget.report.entity.po.BudgetUploadLog;
import com.aacoptics.budget.report.entity.po.ResearchBudget;
import com.aacoptics.common.web.entity.param.BaseParam;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchBudgetQueryParam extends BaseParam<ResearchBudget> {

    /**
     * 上传日志ID
     */
    private Long uploadLogId;

}
