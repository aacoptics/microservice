package com.aacoptics.sep.provider;


import com.aacoptics.sep.entity.SepTokenResult;
import com.aacoptics.sep.entity.form.ChangeForm;
import com.aacoptics.sep.entity.form.LoginForm;
import com.aacoptics.sep.entity.vo.Group;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SepServerProviderTest {
    @Resource
    SepServerProvider sepServerProvider;

    @Autowired
    LoginForm loginForm;

    @Test
    public void Test(){

        List<ChangeForm> changeForms = new ArrayList<>();
        ChangeForm changeForm = new ChangeForm();
        Group group = new Group();
        group.setId("F23D06720AE94132370C311A70785B4A");
        changeForm.setGroup(group);
        changeForm.setHardwareKey("AE12FD4000C600F21BF5A9B463BCD7EC");
        changeForms.add(changeForm);

        JSONObject res3 = sepServerProvider.changeClientGroup(changeForms, "Bearer ea28412f-d5b7-4f34-89cd-ed0e8a6133cb");
        SepTokenResult res = sepServerProvider.getToken(loginForm);
        SepTokenResult res1 = sepServerProvider.getToken(loginForm);

    }

}