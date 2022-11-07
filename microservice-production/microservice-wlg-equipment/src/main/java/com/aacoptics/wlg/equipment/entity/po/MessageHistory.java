package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_message_history")
public class MessageHistory extends BasePo {


    /**
     * 消息业务类型，包括：点检异常消息、保养异常消息、未按时点检消息、未按时保养消息
     */
    @TableField(value = "type")
    private String type;

    /**
     * 工单号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 资产编码
     */
    @TableField(value = "mch_code")
    private String mchCode;

    /**
     * 飞书消息接收者类型
     */
    @TableField(value = "receive_type")
    private String receiveType;

    /**
     * 接收ID
     */
    @TableField(value = "receive_id")
    private String receiveId;


    /**
     * 消息内容
     */
    @TableField(value = "message")
    private String message;

}
