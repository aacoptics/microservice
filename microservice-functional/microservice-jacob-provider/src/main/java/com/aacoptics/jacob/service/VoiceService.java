package com.aacoptics.jacob.service;


import com.aacoptics.jacob.entity.vo.VoiceFileInfo;

public interface VoiceService {
    String generateVoiceFile(String filePath, VoiceFileInfo voiceFileInfo);
}