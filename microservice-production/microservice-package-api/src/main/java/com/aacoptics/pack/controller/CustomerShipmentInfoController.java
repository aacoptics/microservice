package com.aacoptics.pack.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.pack.entity.form.CustomerShipmentInfoForm;
import com.aacoptics.pack.entity.po.CustomerShipmentInfo;
import com.aacoptics.pack.entity.po.ShipmentBatchInfo;
import com.aacoptics.pack.exception.UploadErrorType;
import com.aacoptics.pack.service.ICustomerShipmentInfoService;
import com.aacoptics.pack.service.IShipmentBatchInfoService;
import com.aacoptics.pack.service.IUploadPackageInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 数据字典类型表
 *
 * @author yanshangqi
 */
@RestController
@RequestMapping("/customerShipment")
@Api("CustomerShipmentInfoController")
@Slf4j
public class CustomerShipmentInfoController {

    @Resource
    ICustomerShipmentInfoService customerShipmentInfoService;

    @Resource
    IShipmentBatchInfoService shipmentBatchInfoService;

    @Resource
    IUploadPackageInfoService uploadPackageInfoService;

    @ApiOperation(value = "根据客户，订单获取关联数据", notes = "根据客户，订单获取关联数据")
    @GetMapping("/getShipmentInfos")
    public Result getShipmentInfos(@RequestParam Integer page,
                                 @RequestParam Integer size,
                                 @RequestParam String customer,
                                 @RequestParam String orderNo) {
        CustomerShipmentInfo customerShipmentInfo = customerShipmentInfoService.getByOrderNo(customer, orderNo);
        if(customerShipmentInfo != null && customerShipmentInfo.getUploadFlg() == 1)
            return Result.fail(new UploadErrorType("005001", "该订单号已上传！"));

        Page<ShipmentBatchInfo> iPage = new Page<>(page, size);
        Page<ShipmentBatchInfo> res = shipmentBatchInfoService.getShipmentBatchInfo(iPage, customer, orderNo);
        if(res.getTotal() == 0)
            return Result.fail(new UploadErrorType("005000", "查询不到该订单数据！"));
        return Result.success(res);
    }

    @ApiOperation(value = "根据客户，订单获取关联数据", notes = "根据客户，订单获取关联数据")
    @PostMapping("/uploadShipmentInfos")
    public Result uploadShipmentInfos(@RequestBody CustomerShipmentInfoForm customerShipmentInfoForm) {
        CustomerShipmentInfo customerShipmentInfo = customerShipmentInfoService.getByOrderNo(customerShipmentInfoForm.getCustomer(),
                customerShipmentInfoForm.getOrderNo());
        if(customerShipmentInfo != null && customerShipmentInfo.getUploadFlg() == 1)
            return Result.fail(new UploadErrorType("005001", "该订单号已上传！"));

        return uploadPackageInfoService.uploadPackageInfo(customerShipmentInfoForm);
    }
}
