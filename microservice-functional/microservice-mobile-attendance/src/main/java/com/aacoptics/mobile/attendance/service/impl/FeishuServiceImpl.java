package com.aacoptics.mobile.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.aacoptics.mobile.attendance.config.FeishuAppKeyConfig;
import com.aacoptics.mobile.attendance.entity.vo.MockMultipartFile;
import com.aacoptics.mobile.attendance.exception.BusinessException;
import com.aacoptics.mobile.attendance.provider.FeishuApiProvider;
import com.aacoptics.mobile.attendance.service.FeishuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

@Service
@Slf4j
public class FeishuServiceImpl implements FeishuService {

    @Resource
    private FeishuApiProvider feishuApiProvider;

    @Resource
    private FeishuAppKeyConfig feishuAppKeyConfig;

    @Override
    public String fetchAccessToken() {
        final JSONObject result = feishuApiProvider.fetchAccessToken(feishuAppKeyConfig);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);

        return result.get("code", Integer.class) == 0
                ? StrUtil.format("Bearer {}", result.get("tenant_access_token"))
                : null;
    }

    @Override
    public String fetchUploadFileKey(String fileType, byte[] fileContent, Integer duration) throws IOException {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return null;

        InputStream inputStream = new ByteArrayInputStream(fileContent);
        MultipartFile multipartFile = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
        final JSONObject result = feishuApiProvider.fetchUploadFileKey(accessToken, fileType, multipartFile);
        final String throwable = result.get("Throwable", String.class);

        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);

        return result.get("code", Integer.class) == 0
                ? result.get("data", JSONObject.class).get("file_key", String.class)
                : null;
    }
}
