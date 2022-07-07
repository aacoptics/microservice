package com.aacoptics.jacob.util;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;

public class FileUtils {
    public static void responseTo(File file, HttpServletResponse res){  //将文件发送到前端
        res.setContentType("application/octet-stream");
        String fileName = file.getName();
        try {
            fileName = URLEncoder.encode(fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(Files.newInputStream(file.toPath()));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, i);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }

    public static int getMp3Duration(String filePath){
        try{

            File mp3File = new File(filePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);

            MP3AudioHeader audioHeader = f.getMP3AudioHeader();
            return audioHeader.getTrackLength();

        }catch(Exception err){

            return 0;
        }
    }
}
