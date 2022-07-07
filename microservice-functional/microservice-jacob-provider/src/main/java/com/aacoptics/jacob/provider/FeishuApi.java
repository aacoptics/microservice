package com.aacoptics.jacob.provider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FeishuApi {

    @Resource
    private OkHttpCli okHttpCli;

    public String SendGroupMessage(String robotWebhook, String content){
        JSONObject text = new JSONObject();
        String jsonStr = "{\n" +
                "    \"msg_type\":\"audio\",\n" +
                "    \"content\":{\n" +
                "        \"file_key\": \"file_v2_2f4c8c06-a285-4af5-bf53-d84282dc936g\"\n" +
                "    }\n" +
                "}  ";
        JSONObject jsonBody = JSONObject.parseObject(jsonStr);

        return okHttpCli.doPostJson(robotWebhook, jsonBody);
    }
}