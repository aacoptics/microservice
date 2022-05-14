package com.aacoptics.gateway.web.provider;

import com.aacoptics.common.core.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Kaizhi Xuan
 * @date 2021/12/13 17:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class AuthProviderTest {

    @Resource
    private AuthProvider authProvider;

    @Test
    public void test() {
        Result get = authProvider.ignoreAuthentication("/environmentSend/getUploadData", "GET");
    }

}