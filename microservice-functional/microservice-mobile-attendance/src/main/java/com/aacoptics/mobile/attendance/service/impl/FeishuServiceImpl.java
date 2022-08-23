package com.aacoptics.mobile.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.aacoptics.mobile.attendance.config.FeishuAppKeyConfig;
import com.aacoptics.mobile.attendance.exception.BusinessException;
import com.aacoptics.mobile.attendance.provider.FeishuApiProvider;
import com.aacoptics.mobile.attendance.service.FeishuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;

/**
 * @author Kaizhi Xuan
 * created on 2022/6/25 9:17
 */
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
    public String fetchUploadFileKey(String fileType, String filePath, Integer duration) throws IOException {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return null;

        File file = new File(filePath);



        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            IOUtils.copy(fileInputStream, fileItem.getOutputStream());
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            final JSONObject result = feishuApiProvider.fetchUploadFileKey(accessToken, fileType, multipartFile);
            final String throwable = result.get("Throwable", String.class);
            if (StrUtil.isNotEmpty(throwable))
                throw new BusinessException(throwable);

            return result.get("code", Integer.class) == 0
                    ? result.get("data", JSONObject.class).get("file_key", String.class)
                    : null;
        }
    }
}
