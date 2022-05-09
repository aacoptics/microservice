package com.aacoptics.mobile.attendance.config;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.mobile.attendance.entity.AccessTokenResult;
import com.aacoptics.mobile.attendance.provider.FeishuApiProvider;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class FeishuAppKeyConfigTest {

    @Resource
    FeishuAppKeyConfig feishuAppKeyConfig;

    @Resource
    FeishuApiProvider feishuApiProvider;

    @Test
    void test(){
        AccessTokenResult res = feishuApiProvider.GetAccessToken(feishuAppKeyConfig);
        FeishuAppKeyConfig test = feishuAppKeyConfig;
    }
}