package com.aacoptics.pack.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_package_bound_info")
public class ShipmentBatchInfo implements Serializable {

    private Long id;

    private String customer;

    private String customerMaterialNo;

    private String batchName;

    private String supplier;

    private String outerBox;

    private Double outerBoxQty;

    private LocalDateTime outerBoxTime;

    private String spotTicket;

    private Double spotQty;

    private LocalDateTime spotTime;

}