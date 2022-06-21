package com.aacoptics.gaia.provider;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gaia.entity.po.DingTalkMessage;
import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Slf4j
public class GaiaApiProviderFallback implements FallbackFactory<GaiaApiProvider> {

    @Override
    public GaiaApiProvider create(Throwable throwable) {
        return new GaiaApiProvider() {
            @Override
            public JSONObject getToken(String appId,
                                       String secret,
                                       String companyCode) {
                Result res = Result.fail(throwable);
                return (JSONObject)JSONObject.toJSON(res);
            }

            @Override
            public JSONObject getClassInfo(String token) {
                Result res = Result.fail(throwable);
                return (JSONObject)JSONObject.toJSON(res);
            }
        };
    }
}
