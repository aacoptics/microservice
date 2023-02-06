package com.aacoptics.wlg.broadcast.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.wlg.broadcast.entity.form.SpeakerVoiceFileInfo;
import com.aacoptics.wlg.broadcast.exception.BusinessException;
import com.aacoptics.wlg.broadcast.provider.OkHttpCli;
import com.aacoptics.wlg.broadcast.service.BroadcastService;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class BroadcastServiceImpl implements BroadcastService {

    @Resource
    OkHttpCli okHttpCli;

    static final List<SpeakerVoiceFileInfo> speakerInfos = Arrays.asList(
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.125").setSpeakerSn("ls20://02008669A7B1").setSpeakerPort(8888),
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.126").setSpeakerSn("ls20://02026DFB7B6D").setSpeakerPort(8888),
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.127").setSpeakerSn("ls20://020FF3BC43E6").setSpeakerPort(8888),
            new SpeakerVoiceFileInfo().setSpeakerIp("10.7.55.128").setSpeakerSn("ls20://0202C68F8D6B").setSpeakerPort(8888)
    );

    @Async
    public void broadcastAllSpeaker() {
        for (SpeakerVoiceFileInfo speakerInfo : speakerInfos) {
            SpeakerVoiceFileInfo speakerVoiceFileInfo = new SpeakerVoiceFileInfo();
            speakerVoiceFileInfo.setSpeakerIp(speakerInfo.getSpeakerIp()).setSpeakerSn(speakerInfo.getSpeakerSn()).setSpeakerPort(speakerInfo.getSpeakerPort());
            this.broadcastPerformanceDataAvailable(speakerVoiceFileInfo);
        }
    }


    @Override
    public void broadcastPerformanceDataAvailable(SpeakerVoiceFileInfo speakerVoiceFileInfo) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("voice/performanceDataAvailable.mp3");

        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        String voiceFileName = "performanceDataAvailable_" + Calendar.getInstance().getTimeInMillis();

        String tempDir = System.getProperty("java.io.tmpdir");

        String voiceFilePath = tempDir + "\\" + voiceFileName + ".mp3";
        File file = new File(voiceFilePath);
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = bufferedInputStream.read(buffer, 0, 8192)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
                inputStream = null;
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                }
                bufferedOutputStream = null;
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                }
                bufferedInputStream = null;
            }
        }

        try {
            String outFileName = "performanceDataAvailable_" + Calendar.getInstance().getTimeInMillis();
            String outFilePath = voiceFilePath.replace(voiceFileName, outFileName);

            String outFileUrl = this.formatVoiceFile(file, outFilePath);
            JSONObject jsonObject = JSONUtil.createObj()
                    .set("type", "req")
                    .set("name", "songs_queue_append")
                    .set("sn", speakerVoiceFileInfo.getSpeakerSn());
            JSONObject paramsObject = JSONUtil.createObj()
                    .set("tid", "localMusicPlay")
                    .set("vol", 100);
            JSONArray urlArray = JSONUtil.createArray();
            urlArray.add(JSONUtil.createObj()
                    .set("name", outFileName + ".mp3")
                    .set("uri", outFileUrl));
            paramsObject.set("urls", urlArray);
            jsonObject.set("params", paramsObject);
            okHttpCli.doPostJsonSpeaker(StrUtil.format("http://{}:{}", speakerVoiceFileInfo.getSpeakerIp(), speakerVoiceFileInfo.getSpeakerPort()), jsonObject);
            file.delete();
        } catch (Exception e) {
            throw new BusinessException("文件[" + voiceFileName + "]发送至扬声器失败");
        }
    }


    @Override
    public String formatVoiceFile(File inputFile, String outputFilePath) throws EncoderException, UnknownHostException {
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);//设置比特率
        audio.setSamplingRate(44100);//设置采样率
        audio.setChannels(2);//设置音频通道数
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");//设置格式，我的文件原本就是mp3格式的
        attrs.setAudioAttributes(audio);
        //attrs.setDuration(360f); // 设置截取的时长
        Encoder encoder = new Encoder();
        File outputFile = new File(outputFilePath);
        encoder.encode(inputFile,outputFile, attrs);
        return StrUtil.format("http://{}:8888/{}",getLocalhostAddress(),outputFile.getName());
    }

    public String getLocalhostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}