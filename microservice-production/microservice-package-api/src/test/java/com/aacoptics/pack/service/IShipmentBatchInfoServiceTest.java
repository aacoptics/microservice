package com.aacoptics.pack.service;


import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.aacoptics.pack.provider.QtPackageProvider;
import com.aacoptics.pack.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IShipmentBatchInfoServiceTest {

    @Resource
    IShipmentBatchInfoService shipmentBatchInfoService;

    @Resource
    QtPackageProvider qtPackageProvider;

    @Resource
    IUploadPackageInfoService uploadPackageInfoService;



    @Test
    public void test(){
        uploadPackageInfoService.uploadPackageInfo(uploadPackageInfoService.getQtPackageInfo("丘钛", "12345678901234", "test", "test1"));

        String asd = CommonUtil.flushLeft("0", 6, "asdfg");
        QtPackageProvider.QtUserInfo test6 = new QtPackageProvider.QtUserInfo("admin", "GQDstcKsx0");
        JSONObject test2 = qtPackageProvider.getToken(test6);
        List<ShipmentBatchInfo> test = shipmentBatchInfoService.getShipmentBatchInfo("丘钛", "12345678901234");
        List<ShipmentBatchInfo> test1 = shipmentBatchInfoService.getShipmentBatchInfo("丘钛", "12345678901234");
    }

}