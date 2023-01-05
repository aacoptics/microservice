package com.aacoptics.temphumidity.service.impl;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.temphumidity.config.NotifyMemberConfig;
import com.aacoptics.temphumidity.entity.*;
import com.aacoptics.temphumidity.mapper.TemphumidityMapper;
import com.aacoptics.temphumidity.provider.NotificationProvider;
import com.aacoptics.temphumidity.provider.NotificationProviderFallback;
import com.aacoptics.temphumidity.service.TemphumidityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TemphumidityServiceImpl extends ServiceImpl<TemphumidityMapper, TemphumidityInfo> implements TemphumidityService {

    private static final String RECEIVE_ID_TYPE_USER_ID = "user_id";

    private Map<String, String> memberMap = new HashMap<>();

    @Autowired
    TemphumidityMapper temphumidityMapper;

    @Autowired
    NotificationProvider notificationProvider;

    @Resource
    NotifyMemberConfig memberConfig;

    @Override
    public List<TemphumidityInfo> getTemphumidityInfoByDate(String date) {
        return temphumidityMapper.getTemphumidityInfoByDate(date);
    }

    @Override
    public List<SmartMeterInfo> getSmartMeterInfoByDate(String date) {
        return temphumidityMapper.getSmartMeterInfoByDate(date);
    }

    @Override
    public void updateElectricData() {
        for(int i=0;i<100;i++)
        {
            List<SmartMeterInfo> rawDataList = temphumidityMapper.getRawEletricData();
            if(rawDataList == null || rawDataList.isEmpty())
            {
                continue;
            }
            SmartMeterInfo rawData = rawDataList.get(0);

            int deviceId = Integer.parseInt(rawData.getDeviceId() + "");

            String buildingNo = "3";
            String floorNo = "";
            String roomNo = "";
            String meterNo = "";

            String sensorNumber = rawData.getClientId();

            String dataColTime = rawData.getDataColTime();
            String workDate = dataColTime.substring(0, 10);
            String pTotalTxt = rawData.getPTotal();
            BigDecimal increasePower = null;
            BigDecimal powerTotalQty = null;

            int dataId = rawData.getId();
            Map<String, Object> param = new HashMap<>();
            param.put("dataColTime", dataColTime);
            param.put("deviceId", deviceId);

            List<SmartMeterInfo> lastRawDataList = temphumidityMapper.getLastRawEletricData(param);
            if(lastRawDataList == null || lastRawDataList.isEmpty())
            {
                increasePower = BigDecimal.valueOf(Double.parseDouble(pTotalTxt));
            }
            else
            {
                String pTotalTxtLast = lastRawDataList.get(0).getPTotal();
                BigDecimal currentPTotal = BigDecimal.valueOf(Double.parseDouble(pTotalTxt));
                BigDecimal lastPTotal = BigDecimal.valueOf(Double.parseDouble(pTotalTxtLast));

                increasePower = currentPTotal.subtract(lastPTotal);
            }
            BigDecimal powerQty = increasePower;
            List<ElectricData> electricDataList = temphumidityMapper.getTop1ElectricData(deviceId);
            if(electricDataList != null && !electricDataList.isEmpty())
            {
                ElectricData lastRecord = electricDataList.get(0);
                floorNo = lastRecord.getFloorNo();
                roomNo = lastRecord.getRoomNo();
                meterNo = lastRecord.getMeterNo();

                String lastCollectTime = lastRecord.getCollectTime();

                String workDateLast = lastRecord.getWorkDate();
                BigDecimal powerTotalQtyLast =  lastRecord.getPowerTotalQty();
                if(!workDate.equals(workDateLast))
                {
                    powerTotalQty = increasePower;
                }
                else if(dataColTime.compareTo(lastCollectTime) > -1)
                {
                    powerTotalQty = powerTotalQtyLast.add(increasePower);
                }
                else
                {
                    powerTotalQty = powerTotalQtyLast;
                }
            }
            else
            {
                String deviceName = temphumidityMapper.getDeviceNameById(deviceId);
                deviceName = deviceName.replaceAll("智能电表 ", "");
                floorNo = deviceName.split("-")[0];
                roomNo = deviceName.split("-")[1];
                meterNo = deviceName.split("-")[2];

                powerTotalQty = increasePower;
            }

            Map<String, Object> insertParam = new HashMap<>();
            insertParam.put("deviceId", deviceId);
            insertParam.put("buildingNo", buildingNo);
            insertParam.put("floorNo", floorNo);
            insertParam.put("roomNo", roomNo);
            insertParam.put("meterNo", meterNo);
            insertParam.put("powerQty", powerQty.doubleValue());
            insertParam.put("powerTotalQty", powerTotalQty.doubleValue());
            insertParam.put("sensorNumber", sensorNumber);
            insertParam.put("workDate", workDate);
            insertParam.put("dataColTime", dataColTime);
            insertParam.put("dataId", dataId);

            temphumidityMapper.insertElectricData(insertParam);
        }
    }

    @Override
    public void updateTempHumidityData() {

        memberMap = new HashMap<>();
        memberMap.put("1", memberConfig.getOne());
        memberMap.put("2", memberConfig.getTwo());
        memberMap.put("3", memberConfig.getThree());
        memberMap.put("4", memberConfig.getFour());
        memberMap.put("5", memberConfig.getFive());
        memberMap.put("6", memberConfig.getSix());
        memberMap.put("7", memberConfig.getSeven());

        for(int i=0;i<100;i++)
        {
            List<TemphumidityInfo> rawDataList = temphumidityMapper.getRawTempHumidityData();
            if(rawDataList == null || rawDataList.isEmpty())
            {
                continue;
            }
            TemphumidityInfo rawData = rawDataList.get(0);

            int deviceId = Integer.parseInt(rawData.getDeviceId() + "");

            String buildingNo = "3";
            String floorNo = "";

            String deviceName = temphumidityMapper.getDeviceNameById(deviceId);
            deviceName = deviceName.replaceAll("温湿度集中器00", "");
            floorNo = deviceName;

            String temperature = rawData.getTemperature();
            String humidity = rawData.getHumidity();
            String sensorNumber = rawData.getClientId();

            String dataColTime = rawData.getDataColTime();
            String workDate = dataColTime.substring(0, 10);

            String item = "";
            String itemValue = "";

            if(Double.parseDouble(temperature) < 21 || Double.parseDouble(temperature) > 25)
            {
                item = "温度";
                itemValue = temperature;
            }
            if(Double.parseDouble(humidity) < 40 || Double.parseDouble(humidity) > 60)
            {
                if(!"".equals(item))
                {
                    item = "湿度/湿度";
                    itemValue += ("/" + humidity);
                }
                else
                {
                    item = "湿度";
                    itemValue = humidity;
                }
            }

            String contactMemberListTxt = memberMap.get("7");
            String[] contactMemberArray = contactMemberListTxt.split(",");

            if(!"".equals(item) && i == 0)
            {
                StringBuffer contentStringBuffer = new StringBuffer();
                contentStringBuffer.append("**" + item + "报警**  \n");
                contentStringBuffer.append("时间：" + dataColTime + "  \n");
                contentStringBuffer.append("新能源" + buildingNo + "号楼" + floorNo + "楼" + "车间的" + item + "为" + itemValue + "  \n");
                contentStringBuffer.append("超出规定的范围" + "  \n");
                contentStringBuffer.append("请您关注并跟进解决，谢谢" + "  \n");

                String content = contentStringBuffer.toString();

                if(contactMemberArray != null && contactMemberArray.length > 0) {

                    for (String member : contactMemberArray) {
                        FeishuMessage feishuMessage = new FeishuMessage();
                        feishuMessage.setSendType(RECEIVE_ID_TYPE_USER_ID);
                        feishuMessage.setSendId(member);
                        feishuMessage.setContent(content);

                        Result result = notificationProvider.sendFeishuNotification(feishuMessage);
                        if (result.isSuccess()) {
                            log.info("推送点检异常消息到飞书成功，内容:" + content + " 人员：" + member);

                        } else {
                            log.error("推送点检异常消息到飞书失败，失败:" + content + " 人员：" + member);
                        }
                    }
                }
            }

            int dataId = rawData.getId();

            Map<String, Object> insertParam = new HashMap<>();
            insertParam.put("deviceId", deviceId);
            insertParam.put("buildingNo", buildingNo);
            insertParam.put("floorNo", floorNo);
            insertParam.put("temperature", Double.parseDouble(temperature));
            insertParam.put("humidity", Double.parseDouble(humidity));
            insertParam.put("sensorNumber", sensorNumber);
            insertParam.put("workDate", workDate);
            insertParam.put("dataColTime", dataColTime);
            insertParam.put("dataId", dataId);

            temphumidityMapper.insertTempHumidityData(insertParam);

        }
    }

    @Override
    public List<Map<String, Object>> getTemphumidityInfoDisplay(String buildingNo, String floorNo, String startDate, String endDate) {

        List<Map<String, Object>> tempDataList = new ArrayList<>();

        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("buildingNo", buildingNo);
        queryParam.put("floorNo", floorNo);
        queryParam.put("startDate", startDate);
        queryParam.put("endDate", endDate);

        List<Map<String, Object>> resultList = temphumidityMapper.getTemphumidityInfoDisplay(queryParam);
        if(resultList != null && !resultList.isEmpty())
        {
            for(int i=0;i<resultList.size();i++)
            {
                String remark = "";

                Map<String, Object> resultData = resultList.get(i);

                double temp1 = Double.parseDouble(resultData.get("temp1") + "");
                double temp2 = Double.parseDouble(resultData.get("temp2") + "");
                double temp3 = Double.parseDouble(resultData.get("temp3") + "");
                double temp4 = Double.parseDouble(resultData.get("temp4") + "");

                double humidity1 = Double.parseDouble(resultData.get("humidity1") + "");
                double humidity2 = Double.parseDouble(resultData.get("humidity2") + "");
                double humidity3 = Double.parseDouble(resultData.get("humidity3") + "");
                double humidity4 = Double.parseDouble(resultData.get("humidity4") + "");

                if(temp1 < 21 || temp1 > 25)
                {
                    remark += "温度值1(1:30)超出范围";
                }
                if(temp2 < 21 || temp2 > 25)
                {
                    remark += "温度值2(7:30)超出范围";
                }
                if(temp3 < 21 || temp3 > 25)
                {
                    remark += "温度值3(13:30)超出范围";
                }
                if(temp4 < 21 || temp4 > 25)
                {
                    remark += "温度值4(19:30)超出范围";
                }

                if(humidity1 < 40 || humidity1 > 60)
                {
                    remark += "湿度值1(1:30)超出范围";
                }
                if(humidity2 < 40 || humidity2 > 60)
                {
                    remark += "湿度值2(7:30)超出范围";
                }
                if(humidity3 < 40 || humidity3 > 60)
                {
                    remark += "湿度值3(13:30)超出范围";
                }
                if(humidity4 < 40 || humidity4 > 60)
                {
                    remark += "湿度值4(19:30)超出范围";
                }

                resultData.put("seq", i+1);
                resultData.put("remark", remark);

                tempDataList.add(resultData);
            }
        }
        return tempDataList;
    }

}
