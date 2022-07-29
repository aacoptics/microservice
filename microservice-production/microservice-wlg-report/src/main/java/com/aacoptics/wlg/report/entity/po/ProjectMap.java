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
@TableName("project_map")
public class ProjectMap extends BasePo {

    /**
     * 商务项目号
     */
    private String businessProject;


    /**
     * 内部项目
     */
    private String internalProject;


}