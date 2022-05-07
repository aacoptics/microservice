/* ====================================================================================================
 * Copyright© 2021  诚瑞光学  All Rights Reserved
 *
 * ====================================================================================================
 * Change Log
 * ====================================================================================================
 * 2021-03-11     诚瑞光学      [Init] 单点登录服务响应结果Vo类.
 * ==================================================================================================== */
package com.aacoptics.oauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>单点登录服务响应结果Vo类</p>
 *
 * @author 诚瑞光学
 * @version 1.0.0
 * @since jdk 1.8
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SsoResult implements Serializable {
    private static final long serialVersionUID = -8811231491312833562L;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("ADUser")
    private String adUser;

    @JsonProperty("UserName")
    private String userName;

    @JsonProperty("Messages")
    private String messages;

    @Override
    public String toString() {
        return "SsoResultVo{" +
                "Status='" + status + '\'' +
                ", ADUser='" + adUser + '\'' +
                ", UserName='" + userName + '\'' +
                ", Messages='" + messages + '\'' +
                '}';
    }
}
