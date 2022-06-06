package com.aacoptics.notification.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DingTalkUtil {

    private static Logger logger = LoggerFactory.getLogger(DingTalkUtil.class);

    public static void sendCardCorpConversation(String accessToken, long agentId, String useridList, String title, String markdown, String singleTile, String singleUrl) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(agentId);
        if(!"".equals(useridList))
        {
            request.setUseridList(useridList);
            request.setToAllUser(false);
        }
        else
        {
            request.setToAllUser(true);
        }

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();

        msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
        msg.setMsgtype("action_card");
        msg.getActionCard().setTitle(title);
        msg.getActionCard().setMarkdown(markdown);
        msg.getActionCard().setSingleTitle(singleTile);
        msg.getActionCard().setSingleUrl(singleUrl);

        request.setMsg(msg);

        OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken);
        logger.info(rsp.getBody());
    }

    /**
     * 销售数据推送到钉钉群
     *
     * @param serverUrl 销售数据推送钉钉机器人地址
     * @param title
     * @param message
     * @throws ApiException
     */
    public static Map<String, String> sendGroupRobotMessage(String serverUrl, String title, String message) throws ApiException {

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
        logger.info(response.getBody());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result", response.isSuccess()+"");
        resultMap.put("message", response.getMessage());

        return resultMap;
    }

    public static void sendNotifyByRobot(String title, String content, String robotName, String atMobiles, boolean isAtAll) throws Exception {
        Map<String, Object> param = new HashMap<>();

        /*Map<String, Object> markdown = new HashMap<>();
        markdown.put("title", title);
        markdown.put("text", content);*/

        param.put("msgtype", "markdown");
        param.put("content", content);
        param.put("title", title);
        param.put("atMobiles", atMobiles);
        param.put("isAtAll", isAtAll);
        // param.put("title", title);
        param.put("robotName", robotName);

        HttpClientUtil.postRequest("http://msg.data.governance.aac.com/dwapi-boot/bz/bzDdRobot/sendTextMsg", param);
    }
}
