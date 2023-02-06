package com.aacoptics.wlg.broadcast.service.impl;

import com.aacoptics.wlg.broadcast.exception.BusinessException;
import com.aacoptics.wlg.broadcast.service.BroadcastService;
import it.sauronsoftware.jave.EncoderException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.UnknownHostException;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j

class BroadcastServiceImplTest {

    @Autowired
    private BroadcastService broadcastService;

    @Test
    void test() throws UnknownHostException, EncoderException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("voice/performanceDataAvailable.mp3");

        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        String voiceFileName = "performanceDataAvailable_" + Calendar.getInstance().getTimeInMillis();

        String tempDir = System.getProperty("java.io.tmpdir");

        String voiceFilePath = tempDir + "\\" + voiceFileName + ".mp3";
        File file = new File(voiceFilePath);
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = bis.read(buffer, 0, 8192)) != -1) {
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
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
                bis = null;
            }
        }

        String outFileName = "performanceDataAvailable_" + Calendar.getInstance().getTimeInMillis();;
        String outFilePath = voiceFilePath.replace(voiceFileName, outFileName);

        String outFileUrl = broadcastService.formatVoiceFile(file, outFilePath);

        System.out.println(outFileUrl);
    }


    @Test
    void broadcastAllSpeaker() {
        broadcastService.broadcastAllSpeaker();

    }
}