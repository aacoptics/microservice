package com.aacoptics.okr.core.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("FEISHU_USER")
public class FeishuUser implements Serializable {

    @TableField(value = "COUNTRY")
    private String country;

    @TableField(value = "WORK_STATION")
    private String workStation;

    @TableField(value = "GENDER")
    private String gender;

    @TableField(value = "CITY")
    private String city;

    @TableField(value = "OPEN_ID")
    private String openId;

    @TableField(value = "MOBILE")
    private String mobile;

    @TableField(value = "DESCRIPTION")
    private String description;

    @TableField(value = "EMPLOYEE_NO")
    private String employeeNo;

    @TableField(value = "DEPARTMENT_IDS")
    private String departmentIds;

    @TableField(value = "JOIN_TIME")
    private String joinTime;

    @TableField(value = "EMPLOYEE_TYPE")
    private String employeeType;

    @TableField(value = "USER_ID")
    private String userId;

    @TableField(value = "NAME")
    private String name;

    @TableField(value = "UNION_ID")
    private String unionId;

    @TableField(value = "EN_NAME")
    private String enName;

    @TableField(value = "IS_TENANT_MANAGER")
    private String isTenantManager;

    @TableField(value = "JOB_TITLE")
    private String jobTitle;

    @TableField(value = "EMAIL")
    private String email;

    @TableField(value = "MOBILE_VISIBLE")
    private String mobileVisible;

    @TableField(value = "IS_ACTIVATED")
    private String isActivated;

    @TableField(value = "IS_FROZEN")
    private String isFrozen;

    @TableField(value = "IS_RESIGNED")
    private String isResigned;

    @TableField(value = "IS_UNJOIN")
    private String isUnjoin;

    @TableField(value = "IS_EXITED")
    private String isExited;

    @TableField(value = "LEADER_USER_ID")
    private String leaderUserId;

}
