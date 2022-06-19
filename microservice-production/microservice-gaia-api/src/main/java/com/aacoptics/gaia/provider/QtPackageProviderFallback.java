package com.aacoptics.gaia.provider;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QtPackageProviderFallback implements QtPackageProvider {

    @Override
    public JSONObject getToken(QtUserInfo userInfo) {
        return (JSONObject) new JSONObject().put("Success", false);
    }

    @Override
    public JSONObject uploadQtPackageInfo(JSONObject qtPackageParam, String token) {
        return (JSONObject) new JSONObject().put("Success", false);
    }
}
