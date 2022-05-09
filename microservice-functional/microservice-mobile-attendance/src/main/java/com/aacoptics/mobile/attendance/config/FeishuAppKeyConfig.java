package com.aacoptics.mobile.attendance.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "feishu")
public class FeishuAppKeyConfig implements Serializable {

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("app_secret")
    private String appSecret;
}
