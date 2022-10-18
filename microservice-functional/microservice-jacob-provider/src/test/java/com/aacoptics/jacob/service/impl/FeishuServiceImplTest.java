package com.aacoptics.jacob.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.jacob.entity.vo.SpeakerVoiceFileInfo;
import com.aacoptics.jacob.entity.vo.VoiceFileInfo;
import com.aacoptics.jacob.provider.FeishuApi;
import com.aacoptics.jacob.provider.OkHttpCli;
import com.aacoptics.jacob.service.FeishuService;
import com.aacoptics.jacob.service.VoiceService;
import com.aacoptics.jacob.util.FileUtils;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FeishuServiceImplTest {

    @Resource
    FeishuService feishuService;

    @Resource
    FeishuApi feishuApi;

    @Resource
    VoiceService voiceService;

    @Resource
    OkHttpCli okHttpCli;

    @Test
    public void fetchAccessToken() throws EncoderException, UnknownHostException {
        SpeakerVoiceFileInfo speakerVoiceFileInfo = new SpeakerVoiceFileInfo();
        speakerVoiceFileInfo.setSpeakerIp("10.7.55.125").setSpeakerSn("ls20://02008669A7B1").setSpeakerPort(8888).setMessage("换料提醒！");

        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String filePath = voiceService.generateVoiceFile(fileName, speakerVoiceFileInfo);
        File file = new File(filePath);
        if (!file.exists()) {
        }
        String outFileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String outFilePath = voiceService.formatVoiceFile(file, filePath.replace(fileName, outFileName));

        JSONObject object = JSONUtil.createObj()
                .set("type", "req")
                .set("name", "songs_queue_append")
                .set("sn", speakerVoiceFileInfo.getSpeakerSn());
        JSONObject paramsObject = JSONUtil.createObj()
                        .set("tid", "localMusicPlay")
                        .set("vol", 100);
        JSONArray urlArray = JSONUtil.createArray();
        urlArray.add(JSONUtil.createObj()
                .set("name", outFileName + ".mp3")
                .set("uri", outFilePath));
        paramsObject.set("urls", urlArray);
        object.set("params", paramsObject);

        String test = okHttpCli.doPostJson(StrUtil.format("http://{}:{}", speakerVoiceFileInfo.getSpeakerIp(), speakerVoiceFileInfo.getSpeakerPort()), object);

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
    @Test
    public void testGen(){
        SpeakerVoiceFileInfo speakerVoiceFileInfo = new SpeakerVoiceFileInfo();
        speakerVoiceFileInfo.setSpeakerIp("10.7.36.103").setSpeakerSn("ls20://020FF3BC43E6").setSpeakerPort(8888).setMessage("12345");
        voiceService.generateVoiceFile("test", speakerVoiceFileInfo);
    }



    @Test
    public void getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            String test = address.getHostAddress();
            String test1 = address.getHostName();
            String test2 = address.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}