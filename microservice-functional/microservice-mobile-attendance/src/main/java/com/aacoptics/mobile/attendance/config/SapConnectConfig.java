package com.aacoptics.mobile.attendance.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "sap")
public class SapConnectConfig {

    /**
     * 服务器IP
     */
    @JsonProperty("host")
    private String host;

    /**
     * 客户端
     */
    @JsonProperty("clientName")
    private String clientName;

    /**
     * 语言
     */
    @JsonProperty("language")
    private String language;

    /**
     * 用户名
     */
    @JsonProperty("userid")
    private String userid;

    /**
     * 密码
     */
    @JsonProperty("password")
    private String password;

    /**
     * 系统标识号
     */
    @JsonProperty("system")
    private String system;

    /**
     * 最大连接线程
     */
    @JsonProperty("jcoPeakLimit")
    private String jcoPeakLimit;

}
