package com.aacoptics.common.web.entity.param;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by yanshangqi
 */
@Data
public class BaseParam<T extends BasePo> {
    private LocalDateTime createdTimeStart;
    private LocalDateTime createdTimeEnd;

    public QueryWrapper<T> build() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(null != this.createdTimeStart, "created_time", this.createdTimeStart)
                .le(null != this.createdTimeEnd, "created_time", this.createdTimeEnd);
        return queryWrapper;
    }
}
