package com.aacoptics.common.web.entity.enums;

/**
 * @author Kaizhi Xuan
 * created on 2022/5/31 15:46
 */
public enum EditType {

    /**
     * 新增
     */
    ADD(1),

    /**
     * 修改
     */
    UPDATE(2),

    /**
     * 删除
     */
    DELETE(3);

    private final int value;

    EditType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
