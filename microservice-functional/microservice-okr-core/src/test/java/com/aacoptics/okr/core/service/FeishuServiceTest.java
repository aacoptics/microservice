package com.aacoptics.okr.core.service;

import cn.hutool.json.JSONObject;
import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.aacoptics.okr.core.entity.vo.TreeModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FeishuServiceTest {

    @Resource
    FeishuService feishuService;

    @Resource
    ObjectiveDetailService objectiveDetailService;

    @Test
    public void getFeishuUsers() {
        String auth  = feishuService.getUserAuth("a57u2a8b207841dbb13fdbc6657e962f");
        //List<TreeModel> res = objectiveDetailService.getUserObjectiveTree("60054916", 2L);
        //List<FeishuUser> feishuUsers = feishuService.getFeishuUsers("60054916");
        String asd = "";
    }

    @Test
    public void Test1() {
        objectiveDetailService.okrAlignChat("60054916", 2L);
    }
}