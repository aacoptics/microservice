package com.aacoptics.jacob.service.impl;

import cn.hutool.json.JSONUtil;
import com.aacoptics.jacob.provider.FeishuApi;
import com.aacoptics.jacob.service.FeishuService;
import com.aacoptics.jacob.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FeishuServiceImplTest {

    @Resource
    FeishuService feishuService;

    @Resource
    FeishuApi feishuApi;

    @Test
    public void fetchAccessToken() throws IOException {
//
//        int a = FileUtils.getMp3Duration("d:\\Documents and Settings\\60054916\\Desktop\\test\\test.mp3");
//
//        int b = FileUtils.getMp3Duration("C:\\Soft\\VoiceServer\\voice-file\\1657165659783.mp3");
//
        String asd = feishuService.fetchChatIdByRobot("诚瑞高管群");

        String jsonStr = "{\n" +
                "\t\"receive_id\": \"oc_820faa21d7ed275b53d1727a0feaa917\",\n" +
                "\t\"content\": \"{\\\"file_key\\\":\\\"file_v2_xxx\\\"}\",\n" +
                "\t\"msg_type\": \"file\"\n" +
                "} ";



//        String key = feishuService.fetchUploadFileKey(FeishuService.FILE_TYPE_OPUS, "C:\\Soft\\VoiceServer\\voice-file\\1657165659783.mp3", 10);
//
//        feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID, asd, FeishuService.MSG_TYPE_AUDIO, JSONUtil.createObj().set("file_key", key));
//
//
//        //oc_0b61d548c8df1228c9ce8885f847a3c0
//        String asd1 = asd;
    }
}