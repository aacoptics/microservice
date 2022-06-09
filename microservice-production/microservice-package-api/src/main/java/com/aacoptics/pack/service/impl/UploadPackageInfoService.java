package com.aacoptics.pack.service.impl;

import cn.hutool.json.JSONConverter;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.pack.entity.dto.OuterBoxInfo;
import com.aacoptics.pack.entity.dto.QtPackageParam;
import com.aacoptics.pack.entity.dto.SpotTicketInfo;
import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.aacoptics.pack.provider.QtPackageProvider;
import com.aacoptics.pack.service.IShipmentBatchInfoService;
import com.aacoptics.pack.service.IUploadPackageInfoService;
import com.aacoptics.pack.util.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class UploadPackageInfoService implements IUploadPackageInfoService {

    @Resource
    IShipmentBatchInfoService shipmentBatchInfoService;

    @Resource
    QtPackageProvider qtPackageProvider;

    @Value("${aacoptics.factory.code}")
    String factoryCode;

    @Value("${aacoptics.customer.qt.username}")
    String username;

    @Value("${aacoptics.customer.qt.password}")
    String password;

    @Override
    public QtPackageParam getQtPackageInfo(String customer, String orderNo, String asnNo, String emsNo) {
        List<ShipmentBatchInfo> batchInfos = shipmentBatchInfoService.getShipmentBatchInfo(customer, orderNo);
        QtPackageParam qtPackageParam = new QtPackageParam();
        if (batchInfos.size() > 0) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            qtPackageParam.setAsnNo(asnNo);
            qtPackageParam.setEmsNo(emsNo);
            qtPackageParam.setItemCode(CommonUtil.flushLeft("0", 15, batchInfos.get(0).getCustomerMaterialNo()));
            qtPackageParam.setMpartSpec(batchInfos.get(0).getBatchName());
            qtPackageParam.setVendorCode(batchInfos.get(0).getSupplier());
            qtPackageParam.setFactoryname(factoryCode);
            String shipmentBatch = "";
            OuterBoxInfo outerBox = null;
            for (ShipmentBatchInfo batchInfo : batchInfos) {
                if (!batchInfo.getOuterBox().equals(shipmentBatch)) {
                    if(outerBox != null)
                        qtPackageParam.getPalletNoLists().add(outerBox);

                    outerBox = new OuterBoxInfo();
                    outerBox.setPalletNo(batchInfo.getOuterBox());
                    outerBox.setPalletQty(batchInfo.getOuterBoxQty());
                    String week = CommonUtil.getWeekNo(df.format(batchInfo.getOuterBoxTime()), Calendar.SUNDAY);
                    outerBox.setDateCode(CommonUtil.flushLeft("0", 2, week));
                    shipmentBatch = batchInfo.getOuterBox();
                }
                SpotTicketInfo spotTicketInfo = new SpotTicketInfo();
                spotTicketInfo.setCartonNo(batchInfo.getSpotTicket());
                spotTicketInfo.setCartonQty(batchInfo.getSpotQty());
                spotTicketInfo.setMday(df.format(batchInfo.getSpotTime()));
                assert outerBox != null;
                outerBox.getCartonNoLists().add(spotTicketInfo);
            }
        }
        return qtPackageParam;
    }

    @Override
    public Result uploadPackageInfo(QtPackageParam qtPackageParam){
        JSONObject tokenRes = qtPackageProvider.getToken(new QtPackageProvider.QtUserInfo(username, password));
        if(tokenRes.getBoolean("Success") != null && tokenRes.getBoolean("Success")){
            String token = tokenRes.getString("Token");
            String json = JSON.toJSONString(qtPackageParam);
            JSONObject uploadRes = qtPackageProvider.uploadQtPackageInfo(qtPackageParam, token);
        }
        else{
            return Result.fail();
        }
        return Result.success();
    }
}