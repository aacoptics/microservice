package com.aacoptics.bi.sso.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Data
@NoArgsConstructor
public class FeiShuApi {

    @Value("${feishu.bi.appKey}")
    private String biAppKey;
    @Value("${feishu.bi.appSecret}")
    private String biAppSecret;


}
