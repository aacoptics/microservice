package com.aacoptics.mobile.attendance.provider;

import com.aacoptics.mobile.attendance.config.FeishuAppKeyConfig;
import com.aacoptics.mobile.attendance.entity.AccessTokenResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name = "feishu-server", url = "https://open.feishu.cn/open-apis")
public interface FeishuApiProvider {

    @PostMapping(value = "/auth/v3/tenant_access_token/internal", headers = {"Content-Type=application/json;charset=UTF-8"})
    AccessTokenResult GetAccessToken(@RequestBody FeishuAppKeyConfig appKey);


}
