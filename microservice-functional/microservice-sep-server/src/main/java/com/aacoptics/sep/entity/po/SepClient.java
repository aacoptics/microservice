package com.aacoptics.sep.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxueli 2019-05-04 16:43:12
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("SEM_CLIENT")
public class SepClient implements Serializable {

    @TableId("CLIENT_ID")
    private String clientId;

    // 执行器主键ID
    @TableField("COMPUTER_NAME")
    private String computerName;

    @TableField("HARDWARE_KEY")
    private String hardwareKey;

}
