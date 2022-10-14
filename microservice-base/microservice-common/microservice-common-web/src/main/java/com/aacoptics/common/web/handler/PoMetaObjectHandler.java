package com.aacoptics.common.web.handler;

import com.aacoptics.common.core.util.UserContextHolder;
import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
public class PoMetaObjectHandler implements MetaObjectHandler {
    /**
     * 获取当前Token的用户，为空返回默认UDS
     *
     * @return
     */
    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), BasePo.DEFAULT_USERNAME);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setInsertFieldValByName("createdBy", getCurrentUsername(), metaObject);
        this.setInsertFieldValByName("createdTime", LocalDateTime.from(ZonedDateTime.now()), metaObject);
        this.updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName("updatedBy", getCurrentUsername(), metaObject);
        this.setUpdateFieldValByName("updatedTime", LocalDateTime.from(ZonedDateTime.now()), metaObject);
    }
}