package com.aacoptics.wlg.broadcast.service;


import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.broadcast.entity.form.SpeakerVoiceFileInfo;
import it.sauronsoftware.jave.EncoderException;

import java.io.File;
import java.net.UnknownHostException;

public interface BroadcastService {

    void broadcastAllSpeaker();

    /**
     * 广播“性能数据已出，请及时查看”
     *
     * @param speakerVoiceFileInfo
     */
    void broadcastPerformanceDataAvailable(SpeakerVoiceFileInfo speakerVoiceFileInfo);

    String formatVoiceFile(File inputFile, String outputFilePath) throws EncoderException, UnknownHostException;
}