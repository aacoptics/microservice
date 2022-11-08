package com.aacoptics.wlg.equipment.provider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class FeishuApi {

    public JSONObject getMarkdownMessage(String content, String imageKey){
        //config
        JSONObject config = new JSONObject();
        config.put("wide_screen_mode", true);
        config.put("enable_forward", true);

        //elements
        JSONArray elements = new JSONArray();

        JSONObject text = new JSONObject();
        text.put("content", content);
        text.put("tag", "markdown");
        elements.add(text);

        if(StringUtils.isNotEmpty(imageKey)){
            JSONObject textContent = new JSONObject();
            textContent.put("tag","plain_text");
            textContent.put("content","");
            JSONObject elementImg = new JSONObject();
            elementImg.put("tag", "img");
            elementImg.put("img_key", imageKey);
            elementImg.put("mode", "fit_horizontal");
            elementImg.put("alt", textContent);
            elements.add(elementImg);
        }

        JSONObject card = new JSONObject();
        card.put("config", config);
        card.put("elements", elements);
        return card;
    }

}