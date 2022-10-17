package com.aacoptics.hikvision.api.service.impl;

import com.aacoptics.hikvision.api.config.HikvisionAppKeyConfig;
import com.aacoptics.hikvision.api.entity.param.DoorEventParam;
import com.aacoptics.hikvision.api.entity.vo.DoorEventDetail;
import com.aacoptics.hikvision.api.entity.vo.HikvisionApiPage;
import com.aacoptics.hikvision.api.entity.vo.HikvisionApiResult;
import com.aacoptics.hikvision.api.exception.BusinessException;
import com.aacoptics.hikvision.api.exception.CommonErrorType;
import com.aacoptics.hikvision.api.service.HikvisionApiService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HikvisionApiServiceImpl implements HikvisionApiService {

    @Resource
    HikvisionAppKeyConfig hikvisionAppKeyConfig;

    private void setAppAuth() {
        //生产环境
        ArtemisConfig.host = hikvisionAppKeyConfig.getHost();
        ArtemisConfig.appKey = hikvisionAppKeyConfig.getAppKey();
        ArtemisConfig.appSecret = hikvisionAppKeyConfig.getAppSecret();
    }

    @Override
    public HikvisionApiPage<DoorEventDetail> getDoorEvents(DoorEventParam doorEventParam) {
        setAppAuth();
        final String ARTEMIS_PATH = "/artemis";
        final String previewURLsApi = ARTEMIS_PATH + "/api/acs/v1/door/events";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        String body = JSONObject.toJSONString(doorEventParam);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        HikvisionApiResult<HikvisionApiPage<DoorEventDetail>> res =
                JSONObject.parseObject(result,
                        new TypeReference<HikvisionApiResult<HikvisionApiPage<DoorEventDetail>>>() {
                        });

        if (res.getCode().equals("0")) {
            return res.getData();
        } else {
            throw new BusinessException(CommonErrorType.GET_DOOR_EVENT_ERROR);
        }
    }
}