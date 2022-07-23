package com.aacoptics.jacob.service;


import com.aacoptics.jacob.entity.vo.VoiceFileInfo;
import it.sauronsoftware.jave.EncoderException;

import java.io.File;
import java.net.UnknownHostException;

public interface VoiceService {
    String generateVoiceFile(String filePath, VoiceFileInfo voiceFileInfo);

    String formatVoiceFile(File inputFile, String outputFilePath) throws EncoderException, UnknownHostException;
}