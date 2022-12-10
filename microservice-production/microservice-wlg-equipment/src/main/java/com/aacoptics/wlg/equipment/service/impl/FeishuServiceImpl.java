package com.aacoptics.wlg.equipment.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.wlg.equipment.config.FeishuAppKeyConfig;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.provider.FeishuApiProvider;
import com.aacoptics.wlg.equipment.service.FeishuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import reactor.util.function.Tuple2;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
    public String fetchUploadAvatarImageKey(String filePath) throws IOException {
        return fetchUploadImageKey(FeishuService.IMAGE_TYPE_AVATAR, filePath);
    }

    @Override
    public String fetchUploadMessageImageKey(String filePath) throws IOException {
        return fetchUploadImageKey(FeishuService.IMAGE_TYPE_MESSAGE, filePath);
    }


    private String fetchUploadImageKey(String imageType, String filePath) throws IOException {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return null;

        File file = new File(filePath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            IOUtils.copy(fileInputStream, fileItem.getOutputStream());
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            final JSONObject result = feishuApiProvider.fetchUploadImageKey(accessToken, imageType, multipartFile);
            final String throwable = result.get("Throwable", String.class);
            if (StrUtil.isNotEmpty(throwable))
                throw new BusinessException(throwable);

            return result.get("code", Integer.class) == 0
                    ? result.get("data", JSONObject.class).get("image_key", String.class)
                    : null;
        }
    }



    @Override
    public JSONObject sendMessage(String receiveIdType, String receiveId, String messageType, JSONObject message) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return null;
        JSONObject jsonObject = JSONUtil.createObj()
                .set("receive_id", receiveId)
                .set("content", JSONUtil.toJsonStr(message))
                .set("msg_type", messageType);
        final JSONObject result = feishuApiProvider.fetchSendMessageKey(accessToken, receiveIdType, jsonObject);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);

        return result;
//
//        return result.get("code", Integer.class) == 0;
    }
}
