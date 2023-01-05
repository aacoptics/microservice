package com.aacoptics.temphumidity.provider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FeishuApi {

    @Resource
    private OkHttpCli okHttpCli;

    public JSONObject getMarkdownMessage(String content, String imageKey){
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

        if(StrUtil.isNotEmpty(imageKey)){
            JSONObject textContent = new JSONObject();
            textContent.set("tag","plain_text");
            textContent.set("content","");
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

    public String sendGroupMessage(String robotWebhook, JSONObject carJson){
        JSONObject jsonBody = new JSONObject();
        jsonBody.set("msg_type", "interactive");
        jsonBody.set("card", carJson);
        return okHttpCli.doPostJson(robotWebhook, jsonBody);
    }
}