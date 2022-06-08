package com.aacoptics.pack.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_synchronize_customer_shipment_info")
public class CustomerShipmentInfo extends BasePo {

    private String customer;
    private String expressNo;
    private String orderNo;
    private String asnNo;
    private Integer uploadFlg;
    private String errMessage;
    private Integer errCount;
}