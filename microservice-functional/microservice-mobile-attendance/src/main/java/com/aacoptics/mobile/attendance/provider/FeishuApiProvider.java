package com.aacoptics.mobile.attendance.provider;

import cn.hutool.json.JSONObject;
import com.aacoptics.mobile.attendance.config.FeishuAppKeyConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(name = "feishu-server", url = "https://open.feishu.cn/open-apis", fallbackFactory = FeishuApiProviderFallback.class)
public interface FeishuApiProvider {

    @PostMapping(value = "/auth/v3/tenant_access_token/internal", headers = {"Content-Type=application/json;charset=UTF-8"})
    JSONObject fetchAccessToken(@RequestBody FeishuAppKeyConfig appKey);

    @PostMapping(value = "/attendance/v1/files/upload",
            headers = {"Content-Type=multipart/form-data;charset=UTF-8"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject fetchUploadFileKey(@RequestHeader("Authorization") String authorization,
                                  @RequestPart("file_name") String fileName,
                                  @RequestPart("file") MultipartFile file);



}