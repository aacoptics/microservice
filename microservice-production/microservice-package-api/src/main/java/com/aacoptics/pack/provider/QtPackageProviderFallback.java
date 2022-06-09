package com.aacoptics.pack.provider;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.common.web.entity.ResourceDefinition;
import com.aacoptics.pack.entity.dto.QtPackageParam;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class QtPackageProviderFallback implements QtPackageProvider {

    @Override
    public JSONObject getToken(QtUserInfo userInfo) {
        return (JSONObject) new JSONObject().put("Success", false);
    }

    @Override
    public JSONObject uploadQtPackageInfo(QtPackageParam qtPackageParam, String token) {
        return (JSONObject) new JSONObject().put("Success", false);
    }
}
