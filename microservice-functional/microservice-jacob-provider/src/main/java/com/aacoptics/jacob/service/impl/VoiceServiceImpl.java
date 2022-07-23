package com.aacoptics.jacob.service.impl;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.aacoptics.jacob.entity.vo.VoiceFileInfo;
import com.aacoptics.jacob.service.VoiceService;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@Slf4j
public class VoiceServiceImpl implements VoiceService {

    @Value("${jacob.voice.folderPath}")
    private String folderPath;

    @Override
    public String generateVoiceFile(String fileName, VoiceFileInfo voiceFileInfo) {
        try {
            ActiveXComponent ax = new ActiveXComponent("Sapi.SpVoice");
            Dispatch spVoice = ax.getObject();
            ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();
            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();
            //设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            //设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            Dispatch.call(spFileStream, "Open", new Variant(folderPath + fileName), new Variant(3), new Variant(true));
            //设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            //设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(voiceFileInfo.getSpeakVolume()));
            //设置朗读速度
            Dispatch.put(spVoice, "Rate", new Variant(voiceFileInfo.getSpeakRate()));
            //开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(voiceFileInfo.getMessage()));
            //关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);

            spAudioFormat.safeRelease();
            spFileStream.safeRelease();
            spVoice.safeRelease();
            ax.safeRelease();
            return folderPath + fileName;
        } catch (Exception err) {
            log.error("生成文件失败！", err);
            return null;
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