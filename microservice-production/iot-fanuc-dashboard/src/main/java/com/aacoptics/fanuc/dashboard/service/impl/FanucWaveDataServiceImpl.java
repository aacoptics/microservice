package com.aacoptics.fanuc.dashboard.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.aacoptics.fanuc.dashboard.dao.FanucMasterDataMapper;
import com.aacoptics.fanuc.dashboard.dao.FanucWaveDataMapper;
import com.aacoptics.fanuc.dashboard.entity.FanucMasterData;
import com.aacoptics.fanuc.dashboard.entity.param.FanucWaveDataParam;
import com.aacoptics.fanuc.dashboard.entity.po.FanucWaveData;
import com.aacoptics.fanuc.dashboard.service.FanucMasterDataService;
import com.aacoptics.fanuc.dashboard.service.FanucWaveDataService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aacoptics.fanuc.dashboard.util.ExcelUtil.objectToExcelExportParam;
import static com.aacoptics.fanuc.dashboard.util.ExcelUtil.updateBorder;

@Service
@Slf4j
@DS("waveDB")
public class FanucWaveDataServiceImpl extends ServiceImpl<FanucWaveDataMapper, FanucWaveData> implements FanucWaveDataService {

    @Resource
    FanucWaveDataMapper fanucWaveDataMapper;

    @Override
    public List<Integer> selectCycleNos(LocalDateTime startTime, LocalDateTime endTime, String machineNo) {
        LambdaQueryWrapper<FanucWaveData> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(FanucWaveData::getCycleCount)
                .eq(FanucWaveData::getMachineNo, machineNo)
                .between(FanucWaveData::getStartTime, startTime, endTime);
        return fanucWaveDataMapper.selectObjs(lambdaQueryWrapper).stream()
                .map(o -> (Integer) o)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, JSONArray> getFanucWaveData(List<Integer> cycleNos) {
        List<FanucWaveData> fanucWaveDataList = fanucWaveDataMapper.getFanucWaveData(cycleNos);

        JSONArray injectPressureJson = new JSONArray();
        JSONArray analogInput1Json = new JSONArray();
//        String tempWaferId = "0";
//        LocalDateTime minDateTime = LocalDateTime.MIN;
        JSONArray firstArray = new JSONArray();
        firstArray.add("模次号");
        firstArray.add("参数值");
        firstArray.add("timeStamp");
        injectPressureJson.add(firstArray);
        analogInput1Json.add(firstArray);

        for (FanucWaveData fanucWaveData : fanucWaveDataList) {
            JSONArray singleArray = new JSONArray();
            singleArray.add(fanucWaveData.getCycleCount().toString());
            singleArray.add(fanucWaveData.getInjectPressure());
            singleArray.add(fanucWaveData.getTimeStamp());
            injectPressureJson.add(singleArray);

            JSONArray singleArray2 = new JSONArray();
            singleArray2.add(fanucWaveData.getCycleCount().toString());
            singleArray2.add(fanucWaveData.getAnalogInput1());
            singleArray2.add(fanucWaveData.getTimeStamp());
            analogInput1Json.add(singleArray2);
        }

        Map<String, JSONArray> fanucWaveDataMap = new HashMap<>();
        fanucWaveDataMap.put("injectPressure", injectPressureJson);
        fanucWaveDataMap.put("analogInput1", analogInput1Json);

        return fanucWaveDataMap;
    }

    @Override
    public File exportExcel(List<Integer> cycleNos) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "iot-wave-data-" + currentTimeMillis + ".xlsx";
        List<FanucWaveData> fanucWaveDataList = fanucWaveDataMapper.getFanucWaveData(cycleNos);
        if(fanucWaveDataList.size() > 0){
            List<Map<String, Object>> exportParamList = new ArrayList<>();
            exportParamList.add(objectToExcelExportParam("注塑波形数据", "注塑波形数据明细", fanucWaveDataList, FanucWaveData.class));
            Workbook workbook = ExcelExportUtil.exportExcel(exportParamList, ExcelType.XSSF); // 写入workbook
            updateBorder(workbook);
            FileOutputStream out = new FileOutputStream(tempDir + "/" + fileName);
            workbook.write(out);
            out.close();
            workbook.close();
            return new File(tempDir + "/" + fileName);
        }
        return null;
    }
}
