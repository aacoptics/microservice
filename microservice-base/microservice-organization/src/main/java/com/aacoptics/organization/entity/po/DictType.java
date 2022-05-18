package com.aacoptics.organization.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/26 10:16
 * 字典类型表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dict_type")
public class DictType extends BasePo {

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}