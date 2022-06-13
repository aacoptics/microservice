package com.aacoptics.notification.entity.form;

import com.aacoptics.notification.entity.po.XxlJobInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@ApiModel
@Data
public class XxlJobInfoForm extends XxlJobInfo {

    @NotBlank(message = "角色编码不能为空")
    @ApiModelProperty(value = "角色编码")
    private String code;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "角色拥有的资源id列表")
    private Set<Long> resourceIds;

    @ApiModelProperty(value = "角色拥有的菜单id列表")
    private Set<Long> menuIds;

    public XxlJobInfo toPo() {
        XxlJobInfo t = new XxlJobInfo();
        BeanUtils.copyProperties(this, t);
        return t;
    }

    public XxlJobInfo toPo(int id) {
        XxlJobInfo t = new XxlJobInfo();
        t.setId(id);
        BeanUtils.copyProperties(this, t);
        return t;
    }
}
