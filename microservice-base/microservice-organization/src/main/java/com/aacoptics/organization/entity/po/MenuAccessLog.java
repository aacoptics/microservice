package com.aacoptics.organization.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("menu_access_log")
public class MenuAccessLog extends BasePo {
    private String name;
//    private String ip;
    private String os;
    private String platform;
    private String browser;
    private String version;
    private String engine;
    private String engineVersion;
    private String username;
    private LocalDateTime accessTime;
}