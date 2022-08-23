package com.aacoptics.mobile.attendance.service;

import java.io.IOException;

/**
 * @author Kaizhi Xuan
 * created on 2022/6/25 9:17
 */
public interface FeishuService {

    String fetchAccessToken();

    String fetchUploadFileKey(String fileType, byte[] fileContent, Integer duration) throws IOException;
}
