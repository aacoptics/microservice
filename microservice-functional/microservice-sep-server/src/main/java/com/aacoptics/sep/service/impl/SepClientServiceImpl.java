package com.aacoptics.sep.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.sep.constant.SepGroupConstant;
import com.aacoptics.sep.entity.vo.SepTokenResult;
import com.aacoptics.sep.entity.form.ChangeForm;
import com.aacoptics.sep.entity.form.LoginForm;
import com.aacoptics.sep.entity.form.QueryForm;
import com.aacoptics.sep.entity.po.SepClient;
import com.aacoptics.sep.entity.vo.Group;
import com.aacoptics.sep.mapper.SepClientMapper;
import com.aacoptics.sep.provider.SepServerProvider;
import com.aacoptics.sep.service.SepClientService;
import com.aacoptics.common.core.vo.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class SepClientServiceImpl extends ServiceImpl<SepClientMapper, SepClient> implements SepClientService {

    @Resource
    SepServerProvider sepServerProvider;

    @Resource
    SepClientMapper sepClientMapper;

    @Resource
    LoginForm loginForm;

    @Override
    public List<SepClient> getHardwareKey(String computerName) {
        return sepClientMapper.getHardwareKey(computerName.toUpperCase());
    }

    @Override
    public Result ChangeGroup(QueryForm queryForm) {
        List<SepClient> clintInfo = getHardwareKey(queryForm.getComputerName());
        if (clintInfo.size() <= 0) {
            return Result.fail("查询不到该机器");
        }
        List<ChangeForm> changeForms = new ArrayList<>();
        ChangeForm changeForm = new ChangeForm();
        Group group = new Group();
        if (SepGroupConstant.GROUP_MAP.containsKey(queryForm.getGroup())) {
            group.setId(SepGroupConstant.GROUP_MAP.get(queryForm.getGroup()));
        } else {
            group.setId(SepGroupConstant.GROUP_MAP.get("default"));
        }

        changeForm.setGroup(group);
        changeForm.setHardwareKey(clintInfo.get(0).getHardwareKey());
        changeForms.add(changeForm);
        SepTokenResult tokenRes = sepServerProvider.getToken(loginForm);
        if (tokenRes == null || StrUtil.isBlank(tokenRes.getToken()))
            return Result.fail("获取token失败");
        Object res = sepServerProvider.changeClientGroup(changeForms, "Bearer " + tokenRes.getToken());
        JSONArray resJson;
        try {
            resJson = JSONArray.parseArray(JSONArray.toJSONString(res));
        } catch (Exception err) {
            return Result.fail("解析sep返回的结果json失败");
        }
        if (resJson.size() <= 0)
            return Result.fail(res);
        String responseCode = ((JSONObject) resJson.get(0)).getString("responseCode");
        if (responseCode != null && responseCode.equals("200"))
            return Result.success(res);
        else
            return Result.fail(res);
    }
}