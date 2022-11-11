package com.aacoptics.data.analysis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class FtpUtil {

    private static String ftpHostIp;
    private static String ftpPort;
    private static String userName;
    private static String password;

    private static FTPClient ftp;

    /**
     * 连接ftp服务器
     */
    public static void connect() {
        try {
            ftp = new FTPClient();
            if (StringUtils.isNotBlank(ftpPort) && ftpPort.matches("[0-9]+")) {
                ftp.connect(ftpHostIp, Integer.valueOf(ftpPort));
            } else {
                ftp.connect(ftpHostIp, 21);
            }
            boolean loginF = ftp.login(userName, password);
            if (!loginF) {
                log.error("=====FTP Server login fail=====");
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setBufferSize(1024 * 1024 * 10);
            ftp.enterLocalPassiveMode();
            ftp.setRemoteVerificationEnabled(false);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
            log.info("已连接：" + ftpHostIp + ":" + ftpPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public static void closeFtp() {
        if (ftp != null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建并切换到目录结构
     *
     * @param dirTmp
     */
    public static void changeWorkingDirectory(String dirTmp) {
        try {
            String dir = new String(dirTmp.getBytes("UTF-8"), "iso-8859-1");
            boolean cwd = ftp.changeWorkingDirectory(dir);
            if (!cwd) {
                StringBuilder path = new StringBuilder();
                String[] folders = dir.split("/");
                for (String folder : folders) {
                    path.append("/").append(folder);
                    FTPFile ftpFile = ftp.mdtmFile(path.toString());
                    if (ftpFile == null) {
                        ftp.makeDirectory(path.toString());
                    }
                }
                log.info("==WorkingDirectory:" + path.toString());
                ftp.changeWorkingDirectory(path.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param is
     * @param fileName
     */
    public static void uploadFile(InputStream is, String fileName) {
        try {
            //String ftpFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            String ftpFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            ftp.storeFile(ftpFileName, is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定文件文件流
     *
     * @param fileName
     * @return
     */
    public static InputStream getInputStream(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = ftp.retrieveFileStream(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    // 删除指定文件
    public static boolean deleteFile(String fileName) {
        if (ftp == null || !ftp.isConnected()) {
            return false;
        }
        try {
            if (!StringUtils.isEmpty(fileName)) {
                ftp.deleteFile(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Value("${ftp.host}")
    public void setFtpHostIp(String ftpHostIp) {
        this.ftpHostIp = ftpHostIp;
    }

    @Value("${ftp.port}")
    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    @Value("${ftp.user}")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Value("${ftp.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    public static String getFtpPort() {
        return ftpPort;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getFtpHostIp() {
        return ftpHostIp;
    }
}
