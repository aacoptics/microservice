package com.aacoptics.okr.core.provider;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.okr.core.config.FeishuAppKeyConfig;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Kaizhi Xuan
 * created on 2022/6/25 9:19
 */
@Component
@Slf4j
public class FeishuApiProviderFallback implements FallbackFactory<FeishuApiProvider> {
    @Override
    public FeishuApiProvider create(Throwable throwable) {
        return new FeishuApiProvider() {
            @Override
            public JSONObject fetchAccessToken(FeishuAppKeyConfig appKey) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchGetUserAuth(@RequestHeader("Authorization") String authorization,
                                        @RequestBody JSONObject message){
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }
            @Override
            public JSONObject fetchUploadImageKey(String authorization, String imageType, MultipartFile file) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchUploadFileKey(String authorization, String fileType, String fileName, Integer duration, MultipartFile file) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchSendMessageKey(String authorization, String receiveIdType, JSONObject message) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchDeleteMessage(String authorization, String messageId) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchChats(String authorization, String userIdType, String pageToken, int pageSize) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchCreateTask(String authorization,
                                              String userIdType,
                                              JSONObject jsonObject) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchTasks(String authorization,
                                         String userIdType,
                                         String taskId) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

            @Override
            public JSONObject fetchTaskComments(String authorization,
                                                String taskId,
                                                String commentId) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }
        };
    }
}