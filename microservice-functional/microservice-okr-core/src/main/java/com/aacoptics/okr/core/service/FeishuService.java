package com.aacoptics.okr.core.service;

import cn.hutool.json.JSONObject;
import com.aacoptics.okr.core.entity.po.FeishuUser;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Kaizhi Xuan
 * created on 2022/6/25 9:17
 */
public interface FeishuService {

    // 发送消息目标类型Id
    String RECEIVE_ID_TYPE_OPEN_ID = "open_id";
    String RECEIVE_ID_TYPE_USER_ID = "user_id";
    String RECEIVE_ID_TYPE_EMAIL = "email";
    String RECEIVE_ID_TYPE_CHAT_ID = "chat_id";

    // 发送消息类型
    String MSG_TYPE_TEXT = "text";
    String MSG_TYPE_POST = "post";
    String MSG_TYPE_IMAGE = "image";
    String MSG_TYPE_FILE = "file";
    String MSG_TYPE_AUDIO = "audio";
    String MSG_TYPE_MEDIA = "media";
    String MSG_TYPE_STICKER = "sticker";
    String MSG_TYPE_INTERACTIVE = "interactive";
    String MSG_TYPE_SHARE_CHAT = "share_chat";
    String MSG_TYPE_SHARE_USER = "share_user";

    // 上传图片类型
    String IMAGE_TYPE_MESSAGE = "message";
    String IMAGE_TYPE_AVATAR = "avatar";

    // 上传文件类型
    String FILE_TYPE_OPUS = "opus";
    String FILE_TYPE_MP4 = "mp4";
    String FILE_TYPE_PDF = "pdf";
    String FILE_TYPE_DOC = "doc";
    String FILE_TYPE_XLS = "xls";
    String FILE_TYPE_PPT = "ppt";
    String FILE_TYPE_STREAM = "stream";

    FeishuUser getFeishuUser(String employeeNo);

    List<FeishuUser> getFeishuUsers(List<String> employeeNos);

    void sendPersonalMessage(FeishuUser feishuUser, JSONObject cardJson);

    JSONObject getMarkdownMessage(String content, String imageKey);

    List<FeishuUser> getFeishuUsers(String userInfo, String currentUsername);

    List<FeishuUser> getFeishuUsers(String userInfo);

    String fetchAccessToken();

    String fetchUploadAvatarImageKey(String filePath) throws IOException;

    String fetchUploadMessageImageKey(String filePath) throws IOException;

    String fetchUploadFileKey(String fileType, String filePath, Integer duration) throws IOException;

    String fetchChatIdByRobot(String chatName);

    String getUserAuth(String authCode);

    FeishuUser getFeishuUserByAuthCode(String authCode);

    JSONObject sendMessage(String receiveIdType, String receiveId, String messageType, JSONObject message);

    List<FeishuUser> listAllUsers();

    Map<String, List<String>> menuByEmployeeNo(String employeeNo);

    boolean deleteMessage(String messageId);

    JSONObject createTask(String userIdType,
                          JSONObject jsonObject);

    JSONObject getTaskInfo(String taskId);

    JSONObject getTaskCommentsInfo(String taskId,
                                   String CommentId);

    JSONObject getImagePostMessage(String title, List<Tuple2<String, String>> imageTileAndKeys, List<String> atList);

    JSONObject getStringPostMessage(String title, List<String> messages, List<String> atList);

}
