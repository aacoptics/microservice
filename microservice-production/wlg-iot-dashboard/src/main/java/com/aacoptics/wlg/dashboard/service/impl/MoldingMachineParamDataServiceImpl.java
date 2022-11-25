package com.aacoptics.wlg.dashboard.service.impl;

import com.aacoptics.wlg.dashboard.entity.po.MoldingMachineParamData;
import com.aacoptics.wlg.dashboard.entity.po.MoldingParamAnalysisData;
import com.aacoptics.wlg.dashboard.mapper.MoldingMachineParamDataMapper;
import com.aacoptics.wlg.dashboard.mapper.MoldingParamAnalysisDataMapper;
import com.aacoptics.wlg.dashboard.service.MoldingMachineParamDataService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MoldingMachineParamDataServiceImpl extends ServiceImpl<MoldingMachineParamDataMapper, MoldingMachineParamData> implements MoldingMachineParamDataService {
    @Resource
    private MoldingMachineParamDataMapper moldingMachineParamDataMapper;

    @Resource
    private MoldingParamAnalysisDataMapper moldingParamAnalysisDataMapper;

    @Override
    public List<MoldingMachineParamData> getMoldingParamData(String machineName,
                                                             String paramName,
                                                             List<String> waferIds) {
        List<MoldingMachineParamData> moldingMachineParamData = moldingMachineParamDataMapper.getMoldingParamData(machineName, paramName, waferIds);
        if (moldingMachineParamData.size() == 0) {
            return null;
        }
//        String tempWaferId = "0";
//        LocalDateTime minDateTime = LocalDateTime.MIN;
//        for (MoldingMachineParamData moldingMachineParamDatum : moldingMachineParamData) {
//            if (!moldingMachineParamDatum.getWaferId().equals(tempWaferId)) {
//                minDateTime = moldingMachineParamDatum.getPlcTime();
//                tempWaferId = moldingMachineParamDatum.getWaferId();
//            }
//            Duration duration = Duration.between(minDateTime, moldingMachineParamDatum.getPlcTime());
//            moldingMachineParamDatum.setPlcTimeStamp(duration.getSeconds());
//        }
        return moldingMachineParamData;
    }

    @Override
    public JSONArray getMoldingParamDataArray(String machineName,
                                                             String paramName,
                                                             List<String> waferIds) {
        List<MoldingMachineParamData> moldingMachineParamData = moldingMachineParamDataMapper.getMoldingParamData(machineName, paramName, waferIds);
        if (moldingMachineParamData.size() == 0) {
            return null;
        }

        JSONArray res = new JSONArray();
//        String tempWaferId = "0";
//        LocalDateTime minDateTime = LocalDateTime.MIN;
        JSONArray firstArray = new JSONArray();
        firstArray.add("machineName");
        firstArray.add("waferId");
        firstArray.add("paramName");
        firstArray.add("paramValue");
        firstArray.add("plcTime");
        firstArray.add("plcTimeStamp");
        firstArray.add("recipeName");
        firstArray.add("recipePhase");
        firstArray.add("createTime");
        firstArray.add("paramWaferId");
        res.add(firstArray);

        for (MoldingMachineParamData moldingMachineParamDatum : moldingMachineParamData) {
//            if (!moldingMachineParamDatum.getWaferId().equals(tempWaferId)) {
//                minDateTime = moldingMachineParamDatum.getPlcTime();
//                tempWaferId = moldingMachineParamDatum.getWaferId();
//            }
//            Duration duration = Duration.between(minDateTime, moldingMachineParamDatum.getPlcTime());
//            moldingMachineParamDatum.setPlcTimeStamp(duration.getSeconds());
            JSONArray singleArray = new JSONArray();
            singleArray.add(moldingMachineParamDatum.getMachineName());
            singleArray.add(moldingMachineParamDatum.getWaferId());
            singleArray.add(paramName);
            singleArray.add(moldingMachineParamDatum.getParamValue());
            singleArray.add(moldingMachineParamDatum.getPlcTime());
            singleArray.add(moldingMachineParamDatum.getPlcTimeStamp());
            singleArray.add(moldingMachineParamDatum.getRecipeName());
            singleArray.add(moldingMachineParamDatum.getRecipePhase().trim());
            singleArray.add(moldingMachineParamDatum.getCreateTime());
            singleArray.add(moldingMachineParamDatum.getWaferId() + "-" + paramName);
            res.add(singleArray);
        }
        return res;
    }

    @Override
    public List<MoldingMachineParamData> getMachineName() {
        return moldingMachineParamDataMapper.getMachineName();
    }

    @Override
    public List<MoldingMachineParamData> getWaferIds(String machineName,
                                                     LocalDateTime startTime,
                                                     LocalDateTime endTime) {
        return moldingMachineParamDataMapper.getWaferIds(machineName, startTime, endTime);
    }

    @Override
    public List<MoldingMachineParamData> getMoldingParamName(String machineName,
                                                             List<String> waferIds) {
        List<MoldingMachineParamData> moldingMachineParamData = moldingMachineParamDataMapper.getMoldingParamName(machineName, waferIds);
        return moldingMachineParamData;
    }

    @Override
    public List<MoldingParamAnalysisData> getAnalysisData(String machineName,
                                                          List<String> paramNames,
                                                          LocalDateTime startTime,
                                                          LocalDateTime endTime) {
        return moldingParamAnalysisDataMapper.getAnalysisData(startTime, endTime, machineName, paramNames);
    }
}