package com.aacoptics.gaia.provider;
import com.aacoptics.common.core.exception.BaseException;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gaia.entity.po.DingTalkMessage;
import com.aacoptics.gaia.exception.CommonErrorType;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationProviderFallback implements NotificationProvider {

    @Override
    public Result sendDingTalkNotification(DingTalkMessage dingTalkMessage) {
        return Result.fail("消息发送失败！");
    }
}
