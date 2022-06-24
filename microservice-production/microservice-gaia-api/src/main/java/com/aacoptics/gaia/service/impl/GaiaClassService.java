package com.aacoptics.gaia.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gaia.entity.po.DingTalkMessage;
import com.aacoptics.gaia.entity.po.GaiaClass;
import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.aacoptics.gaia.entity.vo.MessageInfo;
import com.aacoptics.gaia.mapper.GaiaClassMapper;
import com.aacoptics.gaia.mapper.PlanActualPerPersonMapper;
import com.aacoptics.gaia.provider.GaiaApiProvider;
import com.aacoptics.gaia.provider.NotificationProvider;
import com.aacoptics.gaia.service.IGaiaClassService;
import com.aacoptics.gaia.service.IPlanActualPerPersonService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GaiaClassService extends ServiceImpl<GaiaClassMapper, GaiaClass> implements IGaiaClassService {

    @Value("${aacoptics.gaia.appId}")
    String appId;

    @Value("${aacoptics.gaia.secret}")
    String secret;

    @Value("${aacoptics.gaia.companyCode}")
    String companyCode;

    @Resource
    GaiaApiProvider gaiaApiProvider;

    @Override
    public void mergeGaiaClass(String classId, String classDesc){
        QueryWrapper<GaiaClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Class_ID", classId)
        .eq("state", "1");
        GaiaClass gaiaClass = this.getOne(queryWrapper);
        if(gaiaClass != null){
            gaiaClass.setClassDes(classDesc)
                    .setModify("uds")
                    .setUpdateTime(LocalDateTime.now());
            this.updateById(gaiaClass);
        }
        else{
            gaiaClass = new GaiaClass();
            gaiaClass.setClassID(classId)
                    .setClassDes(classDesc)
                    .setState("1")
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateTime(LocalDateTime.now())
                    .setCreator("uds")
                    .setModify("uds");
            this.save(gaiaClass);
        }
    }

    @Override
    public void GetClassInfoFromGaia(){
        JSONObject tokenRes = gaiaApiProvider.getToken(appId, secret, companyCode);
        if(JSONUtil.isNull(tokenRes) &&
                tokenRes.getBoolean("result") != null &&
                tokenRes.getBoolean("result")){
            String token = tokenRes.getJSONObject("data").getString("accessToken");
            JSONObject classRes = gaiaApiProvider.getClassInfo(token);
            if(JSONUtil.isNull(classRes) &&
                    classRes.getBoolean("result") != null &&
                    classRes.getBoolean("result")){
                JSONArray classArray = classRes.getJSONArray("data");
                if(classArray.size() > 0){
                    for (Object o : classArray) {
                        JSONObject info = (JSONObject)JSONObject.toJSON(o);
                        String classId = info.getString("Class_ID");
                        String classDesc = info.getString("Class_Desp");
                        mergeGaiaClass(classId, classDesc);
                    }
                }

            }else{
                log.error("获取盖亚Class Info失败！" + classRes);
            }

        }else{
            log.error("获取盖亚token失败！" + tokenRes);
        }

    }
}