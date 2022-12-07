package com.aacoptics.okr.core.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.okr.core.config.FeishuAppKeyConfig;
import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.aacoptics.okr.core.exception.BusinessException;
import com.aacoptics.okr.core.mapper.FeishuUserMapper;
import com.aacoptics.okr.core.mapper.ObjectiveDetailMapper;
import com.aacoptics.okr.core.provider.FeishuApiProvider;
import com.aacoptics.okr.core.service.FeishuService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private FeishuUserMapper feishuUserMapper;

    @Resource
    private ObjectiveDetailMapper objectiveDetailMapper;

    @Override
    public FeishuUser getFeishuUser(String employeeNo) {
        return feishuUserMapper.selectOne(new LambdaQueryWrapper<FeishuUser>().eq(FeishuUser::getEmployeeNo, employeeNo));
    }

    @Override
    public List<FeishuUser> getFeishuUsers(List<String> employeeNos) {
        return feishuUserMapper.selectList(new LambdaQueryWrapper<FeishuUser>().in(FeishuUser::getEmployeeNo, employeeNos));
    }

    @Override
    public List<FeishuUser> listAllUsers() {
        return feishuUserMapper.selectList(new LambdaQueryWrapper<FeishuUser>()
                .eq(FeishuUser::getIsFrozen, '0')
                .eq(FeishuUser::getIsResigned, '0'));
    }

    @Override
    public Map<String, List<String>> menuByEmployeeNo(String employeeNo) {
        return MapUtil.builder(new HashMap<String, List<String>>())
                .put("lead", feishuUserMapper.employeeNoToLead(employeeNo))
                .put("sameLevel", feishuUserMapper.employeeNoToSameLevel(employeeNo))
                .put("atUser", objectiveDetailMapper.employeeNoToAtUser(employeeNo))
                .build();
    }

    @Override
    public JSONObject getMarkdownMessage(String content, String imageKey) {
        //config
        JSONObject config = new JSONObject();
        config.set("wide_screen_mode", true);
        config.set("enable_forward", true);

        //elements
        JSONArray elements = new JSONArray();

        JSONObject text = new JSONObject();
        text.set("content", content);
        text.set("tag", "markdown");
        elements.add(text);

        if (StrUtil.isNotEmpty(imageKey)) {
            JSONObject textContent = new JSONObject();
            textContent.set("tag", "plain_text");
            textContent.set("content", "");
            JSONObject elementImg = new JSONObject();
            elementImg.set("tag", "img");
            elementImg.set("img_key", imageKey);
            elementImg.set("mode", "fit_horizontal");
            elementImg.set("alt", textContent);
            elements.add(elementImg);
        }

        JSONObject card = new JSONObject();
        card.set("config", config);
        card.set("elements", elements);
        return card;
    }

    @Override
    public void sendPersonalMessage(FeishuUser feishuUser, JSONObject cardJson) {

        try {
            JSONObject resultBySendMsg = sendMessage(FeishuService.RECEIVE_ID_TYPE_USER_ID, feishuUser.getUserId(), FeishuService.MSG_TYPE_INTERACTIVE, cardJson);

            if (resultBySendMsg.get("code", Integer.class) == 0) {
                log.info("推送消息成功！用户：{" + feishuUser.getName() + "}");
            } else {
                log.error("推送消息失败！用户：{" + feishuUser.getName() + "}");
            }
        } catch (Exception err) {
            log.error("推送消息失败！用户：{" + feishuUser.getName() + "}" + err.getMessage());
        }

    }

    @Override
    public FeishuUser getFeishuUserByAuthCode(String authCode) {
        String unionId = getUserAuth(authCode);
        if (StrUtil.isBlank(unionId))
            return null;

        return feishuUserMapper.selectOne(new LambdaQueryWrapper<FeishuUser>().eq(FeishuUser::getUnionId, unionId));
    }


    @Override
    public List<FeishuUser> getFeishuUsers(String userInfo, String currentUsername) {
        return feishuUserMapper.selectList(new LambdaQueryWrapper<FeishuUser>()
                .like(FeishuUser::getName, userInfo)
                .ne(FeishuUser::getEmployeeNo, currentUsername));
    }

    @Override
    public List<FeishuUser> getFeishuUsers(String userInfo) {
        return feishuUserMapper.selectList(new LambdaQueryWrapper<FeishuUser>()
                .like(FeishuUser::getName, userInfo)
                .eq(FeishuUser::getIsFrozen, '0')
                .eq(FeishuUser::getIsResigned, '0'));
    }

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

    @Override
    public String getUserAuth(String authCode) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return null;

        JSONObject body = JSONUtil.createObj();
        body.set("grant_type", "authorization_code");
        body.set("code", authCode);

        final JSONObject result = feishuApiProvider.fetchGetUserAuth(accessToken, body);

        return result.get("code", Integer.class) == 0
                ? result.get("data", JSONObject.class).getStr("union_id")
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

    @Override
    public boolean deleteMessage(String messageId) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) return false;

        final JSONObject result = feishuApiProvider.fetchDeleteMessage(accessToken, messageId);
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
    public JSONObject createTask(String userIdType,
                                 JSONObject jsonObject) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken))
            throw new BusinessException("获取access token失败！");
        final JSONObject result = feishuApiProvider.fetchCreateTask(accessToken, userIdType, jsonObject);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);
        return result;
    }

    @Override
    public JSONObject getTaskInfo(String taskId) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken))
            throw new BusinessException("获取access token报错");
        final JSONObject result = feishuApiProvider.fetchTasks(accessToken, FeishuService.RECEIVE_ID_TYPE_USER_ID, taskId);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);
        return result;
    }

    @Override
    public JSONObject getTaskCommentsInfo(String taskId,
                                          String CommentId) {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken))
            throw new BusinessException("获取access token报错");
        final JSONObject result = feishuApiProvider.fetchTaskComments(accessToken, taskId, CommentId);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);
        return result;
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
