package com.aacoptics.wlg.dashboard.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.wlg.dashboard.config.FeishuAppKeyConfig;
import com.aacoptics.wlg.dashboard.exception.BusinessException;
import com.aacoptics.wlg.dashboard.provider.FeishuApiProvider;
import com.aacoptics.wlg.dashboard.service.IFeishuService;
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
public class FeishuService implements IFeishuService {

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
        return fetchUploadImageKey(IFeishuService.IMAGE_TYPE_AVATAR, filePath);
    }

    @Override
    public String fetchUploadMessageImageKey(String filePath) throws IOException {
        return fetchUploadImageKey(IFeishuService.IMAGE_TYPE_MESSAGE, filePath);
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
            final JSONObject result = feishuApiProvider.fetchUploadFileKey(accessToken, fileType, file.getName(), duration, multipartFile);
            final String throwable = result.get("Throwable", String.class);
            if (StrUtil.isNotEmpty(throwable))
                throw new BusinessException(throwable);

            return result.get("code", Integer.class) == 0
                    ? result.get("data", JSONObject.class).get("file_key", String.class)
                    : null;
        }
    }

    @Override
    public String fetchChatIdByRobot(String chatName) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return null;

        final JSONObject result = feishuApiProvider.fetchChats(accessToken, null, null, 100);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);

        return result.get("code", Integer.class) == 0
                ? result.get("data", JSONObject.class).get("items", JSONArray.class).stream().map(JSONUtil::parseObj)
                .filter(item -> StrUtil.equals(item.get("name", String.class), chatName))
                .findFirst().orElse(JSONUtil.createObj().set("chat_id", StrUtil.EMPTY)).get("chat_id", String.class)
                : null;
    }

    @Override
    public boolean sendMessage(String receiveIdType, String receiveId, String messageType, JSONObject message) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return false;
        JSONObject jsonObject = JSONUtil.createObj()
                .set("receive_id", receiveId)
                .set("content", JSONUtil.toJsonStr(message))
                .set("msg_type", messageType);
        final JSONObject result = feishuApiProvider.fetchSendMessageKey(accessToken, receiveIdType, jsonObject);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);

        return result.get("code", Integer.class) == 0;
    }

    @Override
    public JSONObject getImagePostMessage(String title, List<Tuple2<String, String>> imageTileAndKeys, List<String> atList) {
        final List<List<JSONObject>> content = new ArrayList<>();

        for (String at : atList) {
            content.add(
                    ImmutableList.<JSONObject>builder().add(
                            JSONUtil.createObj()
                                    .set("tag", "at")
                                    .set("user_id", at)
                    ).build()
            );
        }

        for (Tuple2<String, String> imageTileAndKey : imageTileAndKeys) {
            content.add(
                    ImmutableList.<JSONObject>builder().add(
                            JSONUtil.createObj()
                                    .set("tag", "text")
                                    .set("text", imageTileAndKey.getT1())
                    ).build()
            );
            content.add(
                    ImmutableList.<JSONObject>builder().add(
                            JSONUtil.createObj()
                                    .set("tag", "img")
                                    .set("image_key", imageTileAndKey.getT2())
                    ).build()
            );
        }

        return JSONUtil.createObj()
                .set("zh_cn", JSONUtil.createObj()
                        .set("title", title)
                        .set("content", content))
                .set("en_us", JSONUtil.createObj()
                        .set("title", title)
                        .set("content", content));
    }

    @Override
    public JSONObject getStringPostMessage(String title, List<String> messages, List<String> atList) {
        final List<List<JSONObject>> content = new ArrayList<>();

        for (String at : atList) {
            content.add(
                    ImmutableList.<JSONObject>builder().add(
                            JSONUtil.createObj()
                                    .set("tag", "at")
                                    .set("user_id", at)
                    ).build()
            );
        }
        for (String message : messages) {
            content.add(
                    ImmutableList.<JSONObject>builder().add(
                            JSONUtil.createObj()
                                    .set("tag", "text")
                                    .set("text", message)
                    ).build()
            );
        }

        return JSONUtil.createObj()
                .set("zh_cn", JSONUtil.createObj()
                        .set("title", title)
                        .set("content", content))
                .set("en_us", JSONUtil.createObj()
                        .set("title", title)
                        .set("content", content));
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
}
