package com.aacoptics.mobile.attendance.provider;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.mobile.attendance.config.FeishuAppKeyConfig;
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
            public JSONObject fetchUploadFileKey(String authorization, String fileName, MultipartFile file) {
                throwable.printStackTrace();
                return JSONUtil.createObj().set("Throwable", throwable.toString());
            }

        };
    }
}