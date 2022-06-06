package com.aacoptics.notification.provider;

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
        text.put("content", content);
        text.put("tag", "lark_md");
        JSONArray elements = new JSONArray();
        JSONObject element = new JSONObject();
        element.put("tag", "div");
        element.put("text", text);
        elements.add(element);

        JSONObject config = new JSONObject();
        config.put("wide_screen_mode", true);
        config.put("enable_forward", true);

        JSONObject card = new JSONObject();
        card.put("config", config);
        card.put("elements", elements);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("msg_type", "interactive");
        jsonBody.put("card", card);

        return okHttpCli.doPostJson(robotWebhook, jsonBody);
    }
}
