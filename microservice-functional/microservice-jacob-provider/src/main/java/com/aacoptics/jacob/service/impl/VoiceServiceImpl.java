package com.aacoptics.jacob.service.impl;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.aacoptics.jacob.entity.vo.VoiceFileInfo;
import com.aacoptics.jacob.service.VoiceService;
//import com.jacob.activeX.ActiveXComponent;
//import com.jacob.com.ComThread;
//import com.jacob.com.Dispatch;
//import com.jacob.com.Variant;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@Slf4j
public class VoiceServiceImpl implements VoiceService {

    @Value("${jacob.voice.folderPath}")
    private String folderPath;

//    @Override
//    public String generateVoiceFileBak(String fileName, VoiceFileInfo voiceFileInfo) {
//        ActiveXComponent ax = null;
//        Dispatch spVoice = null;
//        Dispatch spFileStream = null;
//        Dispatch spAudioFormat = null;
//        try {
//            ComThread.InitMTA();
//            ax = new ActiveXComponent("Sapi.SpVoice");
//            spVoice = ax.getObject();
//            ax = new ActiveXComponent("Sapi.SpFileStream");
//            spFileStream = ax.getObject();
//            ax = new ActiveXComponent("Sapi.SpAudioFormat");
//            spAudioFormat = ax.getObject();
//            //设置音频流格式
//            Dispatch.put(spAudioFormat, "Type", new Variant(22));
//            //设置文件输出流格式
//            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
//            Dispatch.call(spFileStream, "Open", new Variant(folderPath + fileName), new Variant(3), new Variant(true));
//            //设置声音对象的音频输出流为输出文件对象
//            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
//            //设置音量 0到100
//            Dispatch.put(spVoice, "Volume", new Variant(voiceFileInfo.getSpeakVolume()));
//            //设置朗读速度
//            Dispatch.put(spVoice, "Rate", new Variant(voiceFileInfo.getSpeakRate()));
//            //开始朗读
//            Dispatch.call(spVoice, "Speak", new Variant(voiceFileInfo.getMessage()));
//            //关闭输出文件
//            Dispatch.call(spFileStream, "Close");
//            Dispatch.putRef(spVoice, "AudioOutputStream", null);
//            return folderPath + fileName;
//        } catch (Exception err) {
//            log.error("生成文件失败！", err);
//            return null;
//        } finally{
//            try{
//                assert spAudioFormat != null;
//                spAudioFormat.safeRelease();
//                assert spFileStream != null;
//                spFileStream.safeRelease();
//                assert spVoice != null;
//                spVoice.safeRelease();
//                ax.safeRelease();
//                ComThread.Release();
//            }catch (Exception err2){
//                log.error("释放失败！", err2);
//            }
//        }
//    }

    @Override
    public String generateVoiceFile(String fileName, VoiceFileInfo voiceFileInfo){
        BufferedReader br = null;
        try {
            File file = new File(folderPath + "\\daemonTmp");
            File tmpFile = new File(folderPath + "\\daemonTmp\\temp.tmp");//新建一个用来存储结果的缓存文件
            if (!file.exists()){
                file.mkdirs();
            }
            if(!tmpFile.exists()) {
                tmpFile.createNewFile();
            }
            String cmdCode = folderPath + "SystemSpeechTest.exe " + folderPath + fileName + " " + voiceFileInfo.getMessage();
            ProcessBuilder pb = new ProcessBuilder().command("cmd.exe", "/c", cmdCode).inheritIO();
            pb.redirectErrorStream(true);//这里是把控制台中的红字变成了黑字，用通常的方法其实获取不到，控制台的结果是pb.start()方法内部输出的。
            pb.redirectOutput(tmpFile);//把执行结果输出。
            pb.start().waitFor();//等待语句执行完成，否则可能会读不到结果。
            InputStream in = new FileInputStream(tmpFile);
            br= new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            br = null;
            tmpFile.delete();//卸磨杀驴。
            log.info("生成语音文件成功！");
            return folderPath + fileName + ".mp3";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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