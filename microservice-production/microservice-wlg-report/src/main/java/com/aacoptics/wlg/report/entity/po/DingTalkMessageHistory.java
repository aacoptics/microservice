package com.aacoptics.wlg.report.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dingtalk_message_history")
public class DingTalkMessageHistory extends BasePo {

    /**
     * 机器人ID
     */
    private String robotId;


    /**
     * 预估直通率日期
     */
    private LocalDate productionDate;

    /**
     * 推送结果
     */
    private String result;

    /**
     * 推送结果
     */
    private String message;

}