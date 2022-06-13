package com.aacoptics.pack.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.pack.entity.dto.OuterBoxInfo;
import com.aacoptics.pack.entity.dto.QtPackageParam;
import com.aacoptics.pack.entity.dto.SpotTicketInfo;
import com.aacoptics.pack.entity.form.CustomerShipmentInfoForm;
import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.aacoptics.pack.exception.UploadErrorType;
import com.aacoptics.pack.provider.QtPackageProvider;
import com.aacoptics.pack.service.ICustomerShipmentInfoService;
import com.aacoptics.pack.service.IShipmentBatchInfoService;
import com.aacoptics.pack.service.IUploadPackageInfoService;
import com.aacoptics.pack.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class UploadPackageInfoService implements IUploadPackageInfoService {

    @Resource
    IShipmentBatchInfoService shipmentBatchInfoService;

    @Resource
    QtPackageProvider qtPackageProvider;

    @Resource
    ICustomerShipmentInfoService customerShipmentInfoService;

    @Value("${aacoptics.factory.code}")
    String factoryCode;

    @Value("${aacoptics.customer.qt.username}")
    String username;

    @Value("${aacoptics.customer.qt.password}")
    String password;

    @Override
    public QtPackageParam getQtPackageInfo(CustomerShipmentInfoForm customerShipmentInfoForm) {
        List<ShipmentBatchInfo> batchInfos = shipmentBatchInfoService.getShipmentBatchInfo(customerShipmentInfoForm.getCustomer(),
                customerShipmentInfoForm.getOrderNo());
        QtPackageParam qtPackageParam = new QtPackageParam();
        if (batchInfos.size() > 0) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            qtPackageParam.setAsnNo(customerShipmentInfoForm.getAsnNo());
            qtPackageParam.setEmsNo(customerShipmentInfoForm.getExpressNo());
            qtPackageParam.setItemCode(CommonUtil.flushLeft("0", 15, batchInfos.get(0).getCustomerMaterialNo()));
            qtPackageParam.setMpartSpec(batchInfos.get(0).getBatchName());
            qtPackageParam.setVendorCode(batchInfos.get(0).getSupplier());
            qtPackageParam.setFactoryname(factoryCode);
            String shipmentBatch = "";
            OuterBoxInfo outerBox = null;
            for (ShipmentBatchInfo batchInfo : batchInfos) {
                if (!batchInfo.getOuterBox().equals(shipmentBatch)) {
                    if (outerBox != null)
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
        } else {
            return null;
        }
        return qtPackageParam;
    }

    @Override
    public Result uploadPackageInfo(CustomerShipmentInfoForm customerShipmentInfoForm) {
        QtPackageParam qtPackageParam = getQtPackageInfo(customerShipmentInfoForm);
        if (qtPackageParam == null)
            return Result.fail(new UploadErrorType("005000", "查询不到该订单数据！"));
        JSONObject tokenRes = qtPackageProvider.getToken(new QtPackageProvider.QtUserInfo(username, password));
        JSONObject uploadRes;
        CustomerShipmentInfo customerShipmentInfo = customerShipmentInfoService.getByOrderNo(customerShipmentInfoForm.getCustomer(),
                customerShipmentInfoForm.getOrderNo());
        if (customerShipmentInfo == null) {
            customerShipmentInfo = new CustomerShipmentInfo();
            customerShipmentInfo.setAsnNo(qtPackageParam.getAsnNo());
            customerShipmentInfo.setCustomer(customerShipmentInfoForm.getCustomer());
            customerShipmentInfo.setOrderNo(customerShipmentInfoForm.getOrderNo());
            customerShipmentInfo.setExpressNo(qtPackageParam.getEmsNo());
            customerShipmentInfo.setErrCount(0);
        }
        Result finalRes;
        if (tokenRes.getBoolean("Success") != null && tokenRes.getBoolean("Success")) {
            String token = tokenRes.getString("Token");
            uploadRes = qtPackageProvider.uploadQtPackageInfo(qtPackageParam, token);
            log.info(JSONObject.toJSONString(uploadRes));
            if (uploadRes.getInteger("Code") != null && uploadRes.getInteger("Code") == 200) {
                customerShipmentInfo.setUploadFlg(1);
                finalRes = Result.success();
            } else {
                String msg = "未知错误，请联系IT";
                if (!StrUtil.isBlank(uploadRes.getString("Msg"))) {
                    msg = uploadRes.getString("Msg");
                }
                customerShipmentInfo.setErrMessage(msg);
                customerShipmentInfo.setUploadFlg(2);
                customerShipmentInfo.setErrCount(customerShipmentInfo.getErrCount() + 1);
                finalRes = Result.fail(new UploadErrorType("005000", msg));
            }
        } else {
            customerShipmentInfo.setUploadFlg(2);
            customerShipmentInfo.setErrCount(customerShipmentInfo.getErrCount() + 1);
            customerShipmentInfo.setErrMessage("Token获取失败");
            finalRes = Result.fail(new UploadErrorType("004003", "Token获取失败"));
        }
        if(customerShipmentInfo.getId() != null)
            customerShipmentInfoService.updateById(customerShipmentInfo);
        else
            customerShipmentInfoService.add(customerShipmentInfo);
        return finalRes;
    }
}