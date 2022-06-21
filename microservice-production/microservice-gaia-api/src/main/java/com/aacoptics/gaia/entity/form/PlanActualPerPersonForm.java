package com.aacoptics.gaia.entity.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class PlanActualPerPersonForm implements Serializable {

    @NotNull(message = "排班结果ID不能为空")
    private Integer id;

    @NotNull(message = "排班结果信息不能为空")
    private Integer gyTransferFlg;

    private String transferErr;
}
