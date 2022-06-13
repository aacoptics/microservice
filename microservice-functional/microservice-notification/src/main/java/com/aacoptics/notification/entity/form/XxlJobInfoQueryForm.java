package com.aacoptics.notification.entity.form;

import com.aacoptics.notification.entity.param.XxlJobInfoQueryParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@ApiModel
@Data
public class XxlJobInfoQueryForm extends XxlJobInfoQueryParam {

    @ApiModelProperty(value = "计划名称")
    private String jobDesc;

    /**
     * 分页查询的参数，当前页数
     */
    private long current = 1;
    /**
     * 分页查询的参数，当前页面每页显示的数量
     */
    private long size = 10;

    public XxlJobInfoQueryParam toParam() {
        XxlJobInfoQueryParam p = new XxlJobInfoQueryParam();
        BeanUtils.copyProperties(this, p);
        return p;
    }

    /**
     * 从form中获取page参数，用于分页查询参数
     *
     * @return
     */
    public Page getPage() {
        return new Page(this.getCurrent(), this.getSize());
    }
}
