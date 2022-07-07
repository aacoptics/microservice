package com.aacoptics.sale.provider;

import com.aliyun.dingtalktodo_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DingTalkApi {
    @Value("${dingtalk.appKey}")
    private String appKey;
    @Value("${dingtalk.appSecret}")
    private String appSecret;
    @Value("${dingtalk.host}")
    private String BASE_API_CONTENT;//钉钉服务器地址

    public OapiGettokenResponse getAccessToken() throws ApiException {
        DefaultDingTalkClient client = new DefaultDingTalkClient(BASE_API_CONTENT + "/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();

        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        try {
            return client.execute(request);
        } catch (Exception e) {
            try {
                return client.execute(request);
            } catch (Exception ex) {
                return client.execute(request);
            }
        }
    }

    /**
     * 发送文本工作通知
     */
    public void sendTextCorpConversation(String accessToken, String useridList, String message) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(1186196480L);
        request.setUseridList(useridList);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        msg.getText().setContent(message);
        request.setMsg(msg);

        OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken);
        System.out.println(rsp.getBody());

    }


    /**
     * 发送卡片工作通知
     */
    public boolean sendCardCorpConversation(String accessToken, String useridList, String title, String markdown, String singleTile, String singleUrl) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(1186196480L);
        request.setUseridList(useridList);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

//        msg.setMsgtype("action_card");
//        OapiMessageCorpconversationAsyncsendV2Request.ActionCard actionCard = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
//        actionCard.setSingleUrl(singleUrl);
//        actionCard.setSingleTitle(singleTile);
//        actionCard.setMarkdown(markdown);
//        actionCard.setTitle(title);
//        msg.setActionCard(actionCard);

        msg.setMsgtype("markdown");
        msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
        msg.getMarkdown().setText(markdown);
        msg.getMarkdown().setTitle(title);

        request.setMsg(msg);

        OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken);
        log.info("发送工作通知结果", rsp.getBody());
        long errorCode = rsp.getErrcode();
        if(errorCode == 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 销售数据推送到钉钉群
     *
     * @param serverUrl 销售数据推送钉钉机器人地址
     * @param title
     * @param message
     * @throws ApiException
     */
    public Map<String, String> sendGroupRobotMessage(String serverUrl, String title, String message) throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient(serverUrl);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(true);
//        at.setAtMobiles(Arrays.asList("15351344650"));
        request.setAt(at);

        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(message);

        request.setMarkdown(markdown);

        OapiRobotSendResponse response = client.execute(request);
        log.info(response.getBody());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result", response.isSuccess()+"");
        resultMap.put("message", response.getMessage());

        return resultMap;
    }

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public com.aliyun.dingtalktodo_1_0.Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalktodo_1_0.Client(config);
    }

    public String getUnionId(String accessToken, String userId)
    {
        String unionId = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            OapiV2UserGetResponse rsp = client.execute(req, accessToken);
            if(rsp.getResult() == null)
            {
                return unionId;
            }
            unionId = rsp.getResult().getUnionid();
        } catch (ApiException e) {
            log.error("获取UnionId异常", e);
        }

        return unionId;
    }

    /**
     * 创建钉钉待办事项
     *
     * @param accessToken
     * @throws Exception
     */
    public void createDingtalkTodo(String accessToken, String unionId, String sourceId, String subject, String description, String detailUrlStr) throws Exception {
        com.aliyun.dingtalktodo_1_0.Client client = this.createClient();
        CreateTodoTaskHeaders createTodoTaskHeaders = new CreateTodoTaskHeaders();
        createTodoTaskHeaders.xAcsDingtalkAccessToken = accessToken;
        CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs notifyConfigs = new CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs()
                .setDingNotify("1");
        CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl detailUrl = new CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl()
                .setAppUrl(detailUrlStr)
                .setPcUrl(detailUrlStr);
        CreateTodoTaskRequest createTodoTaskRequest = new CreateTodoTaskRequest()
                .setSourceId(sourceId)
                .setSubject(subject)
                .setCreatorId(unionId)
                .setDescription(description)
                .setDueTime(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.ofHours(8)).toEpochMilli())
                .setExecutorIds(Arrays.asList(
                        unionId
                ))
                .setParticipantIds(Arrays.asList(
                        unionId
                ))
                .setDetailUrl(detailUrl)
                .setIsOnlyShowExecutor(true)
                .setPriority(20)
                .setNotifyConfigs(notifyConfigs);
        try {
            CreateTodoTaskResponse createTodoTaskResponse = client.createTodoTaskWithOptions(unionId, createTodoTaskRequest, createTodoTaskHeaders, new RuntimeOptions());
            CreateTodoTaskResponseBody createTodoTaskResponseBody = createTodoTaskResponse.getBody();

            log.info("createTodoTaskResponseBody=" + createTodoTaskResponseBody.toMap());
        } catch (TeaException err) {
            log.error("创建待办事项异常", err);
        } catch (Exception _err) {
            log.error("创建待办事项异常", _err);
        }
    }

    /**
     * 获取钉钉待办事项
     *
     * @param accessToken
     * @throws Exception
     */
    public GetTodoTaskBySourceIdResponseBody getDingtalkTodoTask(String accessToken, String unionId, String sourceId) throws Exception {
        com.aliyun.dingtalktodo_1_0.Client client = this.createClient();
        GetTodoTaskBySourceIdHeaders getTodoTaskBySourceIdHeaders = new GetTodoTaskBySourceIdHeaders();
        getTodoTaskBySourceIdHeaders.xAcsDingtalkAccessToken = accessToken;

        GetTodoTaskBySourceIdResponseBody getTodoTaskBySourceIdResponseBody = null;
        try {
            GetTodoTaskBySourceIdResponse getTodoTaskBySourceIdResponse = client.getTodoTaskBySourceIdWithOptions(unionId, sourceId, getTodoTaskBySourceIdHeaders, new RuntimeOptions());
            getTodoTaskBySourceIdResponseBody = getTodoTaskBySourceIdResponse.getBody();
        } catch (TeaException err) {
            log.error("获取待办事项异常", err);
        } catch (Exception _err) {
            log.error("获取待办事项异常", _err);
        }
        return getTodoTaskBySourceIdResponseBody;
    }


    /**
     * 删除钉钉代办
     * @param accessToken
     * @param unionId
     * @param sourceId
     * @throws Exception
     */
    public void deleteDingtalkTodoTask(String accessToken, String unionId, String sourceId) throws Exception {
        com.aliyun.dingtalktodo_1_0.Client client = this.createClient();
        DeleteTodoTaskHeaders deleteTodoTaskHeaders = new DeleteTodoTaskHeaders();
        deleteTodoTaskHeaders.xAcsDingtalkAccessToken = accessToken;
        DeleteTodoTaskRequest deleteTodoTaskRequest = new DeleteTodoTaskRequest()
                .setOperatorId(unionId);
        try {
            GetTodoTaskBySourceIdResponseBody getTodoTaskBySourceIdResponseBody = this.getDingtalkTodoTask(accessToken, unionId, sourceId);
            String taskId = getTodoTaskBySourceIdResponseBody.getId();

            client.deleteTodoTaskWithOptions(unionId, taskId, deleteTodoTaskRequest, deleteTodoTaskHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            log.error("删除待办事项异常", err);

        } catch (Exception _err) {
            log.error("删除待办事项异常", _err);
        }
    }
}
