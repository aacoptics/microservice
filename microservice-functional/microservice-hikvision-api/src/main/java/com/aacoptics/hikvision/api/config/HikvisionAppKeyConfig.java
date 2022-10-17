package com.aacoptics.hikvision.api.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "hikvision")
public class HikvisionAppKeyConfig implements Serializable {

    private String appKey;

    private String appSecret;

    private String host;
}