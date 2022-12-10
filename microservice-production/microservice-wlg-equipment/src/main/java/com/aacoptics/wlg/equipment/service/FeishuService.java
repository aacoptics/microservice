package com.aacoptics.wlg.equipment.service;

import java.io.IOException;

/**
 * @author Kaizhi Xuan
 * created on 2022/6/25 9:17
 */
public interface FeishuService {

    // 上传图片类型
    String IMAGE_TYPE_MESSAGE = "message";
    String IMAGE_TYPE_AVATAR = "avatar";


    String fetchAccessToken();

    String fetchUploadAvatarImageKey(String filePath) throws IOException;

    String fetchUploadMessageImageKey(String filePath) throws IOException;

}
