package com.aacoptics.wlg.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("mold_use")
public class MoldUse extends BasePo {

    /**
     * 条件代码
     */
    private String code;


    /**
     * 状态
     */
    private String status;


    /**
     * 项目
     */
    private String projectName;

    /**
     * 模具使用日期
     */
    private LocalDate moldDate;

    /**
     * 模具使用数量
     */
    private Long moldQty;

}