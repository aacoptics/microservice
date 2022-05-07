package com.aacoptics.organization.entity.param;

import com.aacoptics.common.web.entity.param.BaseParam;
import com.aacoptics.organization.entity.po.DictData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Kaizhi Xuan
 * @date 2021/11/29 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictDataQueryParam extends BaseParam<DictData> {

    private String dictLabel;

    private String dictValue;

    private String dictType;

    private String status;
}
