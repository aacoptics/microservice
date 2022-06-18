package com.aacoptics.sep.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.sep.entity.SepTokenResult;
import com.aacoptics.sep.entity.form.ChangeForm;
import com.aacoptics.sep.entity.form.LoginForm;
import com.aacoptics.sep.entity.form.QueryForm;
import com.aacoptics.sep.entity.po.SepClient;
import com.aacoptics.sep.entity.vo.Group;
import com.aacoptics.sep.mapper.SepClientMapper;
import com.aacoptics.sep.provider.SepServerProvider;
import com.aacoptics.sep.service.SepClientService;
import com.aacoptics.sep.utils.HttpClientUtil;
import com.aacoptics.common.core.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class SepClientServiceImpl extends ServiceImpl<SepClientMapper, SepClient> implements SepClientService {

    @Resource
    SepServerProvider sepServerProvider;

    @Resource
    SepClientMapper sepClientMapper;

    @Resource
    LoginForm loginForm;

    public static final Map<String, String> GROUP_MAP;

    static {
        GROUP_MAP = new HashMap<>();
        GROUP_MAP.put("01_ImagingDevice", "474A02F50AE94132420A7FCB54116797");
        GROUP_MAP.put("02_UsbKey", "7CE7C1520AE9413208BAECAD7C21CE28");
        GROUP_MAP.put("03_Print", "CDDE0F140AE9413244CEED812EB0CC55");
        GROUP_MAP.put("04_Bluetooth+Print", "49CB77090AE94132301C204163A67C2B");
        GROUP_MAP.put("05_Imaging_Print", "B7EF00910AE941324AFCF00CD3301287");
        GROUP_MAP.put("06_Imaging_Print_UsbKey", "01342B530AE941323DF638E505B8C5D0");
        GROUP_MAP.put("07_COM_sinal", "7B5905B10AE941324477534200DD62A5");
        GROUP_MAP.put("08_only_in", "48BE50180AE9413250845E154C2BD3AC");
        GROUP_MAP.put("default", "F23D06720AE94132370C311A70785B4A");
    }

    @Override
    public List<SepClient> getHardwareKey(String computerName){
        return sepClientMapper.getHardwareKey(computerName.toUpperCase());
    }

    @Override
    public Result ChangeGroup(QueryForm queryForm){
        List<SepClient> clintInfo = getHardwareKey(queryForm.getComputerName());
        if(clintInfo.size() <= 0){
            return Result.fail("查询不到该机器");
        }
        List<ChangeForm> changeForms = new ArrayList<>();
        ChangeForm changeForm = new ChangeForm();
        Group group = new Group();
        if(GROUP_MAP.containsKey(queryForm.getGroup())){
            group.setId(GROUP_MAP.get(queryForm.getGroup()));
        }
        else{
            group.setId(GROUP_MAP.get("default"));
        }

        changeForm.setGroup(group);
        changeForm.setHardwareKey(clintInfo.get(0).getHardwareKey());
        changeForms.add(changeForm);
        SepTokenResult tokenRes = sepServerProvider.getToken(loginForm);
        if(tokenRes == null || StrUtil.isBlank(tokenRes.getToken()))
            return Result.fail("获取token失败");
        String res = HttpClientUtil.doGet("https://10.233.65.50:8446/sepm/api/v1/computers", changeForms, tokenRes.getToken());

        return Result.success(res);
    }
}