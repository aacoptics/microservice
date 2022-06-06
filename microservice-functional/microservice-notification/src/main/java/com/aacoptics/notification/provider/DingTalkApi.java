package com.aacoptics.notification.provider;

import com.aacoptics.common.core.util.UnixTimeUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCspaceAuditlogListRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiCspaceAuditlogListResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

    public OapiCspaceAuditlogListResponse getDingTalkFileLog(String accessToken, LocalDateTime startTime, LocalDateTime endTime, Long gmtCreate, Long bizId){
        Long startTimeUnix = UnixTimeUtil.getTimestampOfDateTime(startTime);
        Long endTimeUnix = UnixTimeUtil.getTimestampOfDateTime(endTime);

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/cspace/auditlog/list");
        OapiCspaceAuditlogListRequest req = new OapiCspaceAuditlogListRequest();
        req.setStartDate(startTimeUnix);
        req.setEndDate(endTimeUnix);
        req.setPageSize(300L);
        if(gmtCreate != null && bizId != null) {
            req.setLoadMoreGmtCreate(gmtCreate);
            req.setLoadMoreBizId(bizId);
        }
        OapiCspaceAuditlogListResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
        } catch (ApiException ex) {
            log.error(ex.getErrMsg());
        }
        return rsp;
    }
}
