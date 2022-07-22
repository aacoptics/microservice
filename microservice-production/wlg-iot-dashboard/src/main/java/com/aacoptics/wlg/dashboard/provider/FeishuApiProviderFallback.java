package com.aacoptics.wlg.dashboard.provider;//package com.aacoptics.mobile.attendance.provider;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.wlg.dashboard.config.FeishuAppKeyConfig;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
            public JSONObject fetchChats(String authorization, String userIdType, String pageToken, int pageSize) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }
        };
    }
}