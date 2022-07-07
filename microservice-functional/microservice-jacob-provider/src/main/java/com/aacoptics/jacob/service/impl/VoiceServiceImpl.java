package com.aacoptics.jacob.service.impl;

import com.aacoptics.jacob.entity.vo.VoiceFileInfo;
import com.aacoptics.jacob.service.VoiceService;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}