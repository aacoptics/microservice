package com.aacoptics.pack.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_customer_shipment_record")
public class CustomerShipmentRecord extends BasePo {

    private Long pbiId;
    private String customer;
    private String orderNo;
}