package com.aacoptics.wlg.dashboard.provider;

import cn.hutool.json.JSONObject;
import com.aacoptics.wlg.dashboard.config.FeishuAppKeyConfig;
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

    @PostMapping(value = "/im/v1/images",
            headers = {"Content-Type=multipart/form-data;charset=UTF-8"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject fetchUploadImageKey(@RequestHeader("Authorization") String authorization,
                                   @RequestPart("image_type") String imageType,
                                   @RequestPart("image") MultipartFile file);

    @PostMapping(value = "/im/v1/files",
            headers = {"Content-Type=multipart/form-data;charset=UTF-8"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject fetchUploadFileKey(@RequestHeader("Authorization") String authorization,
                                  @RequestPart("file_type") String fileType,
                                  @RequestPart("file_name") String fileName,
                                  @RequestPart("duration") Integer duration,
                                  @RequestPart("file") MultipartFile file);

    @PostMapping(value = "/im/v1/messages", headers = {"Content-Type=application/json;charset=UTF-8"})
    JSONObject fetchSendMessageKey(@RequestHeader("Authorization") String authorization,
                                   @RequestParam("receive_id_type") String receiveIdType,
                                   @RequestBody JSONObject message);

    @GetMapping(value = "/im/v1/chats",
            headers = {"Content-Type=multipart/form-data;charset=UTF-8"})
    JSONObject fetchChats(@RequestHeader("Authorization") String authorization,
                          @RequestParam("user_id_type") String userIdType,
                          @RequestParam("page_token") String pageToken,
                          @RequestParam("page_size") int pageSize);

}