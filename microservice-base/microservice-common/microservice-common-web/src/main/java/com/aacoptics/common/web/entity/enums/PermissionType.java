package com.aacoptics.common.web.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Kaizhi Xuan
 * @date 2021/12/13 14:47
 */
public enum PermissionType {
    WHITELIST(0, "白名单"),
    AUTHENTICATION(1, "鉴权");

    @EnumValue
    private final Integer code;

    private final String description;

    PermissionType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static PermissionType getByCode(Integer code) {
        return Arrays.stream(PermissionType.values())
                .filter(p -> Objects.equals(p.getCode(), code))
                .findFirst().orElse(null);
    }

    @JsonValue
    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return description;
    }

}
