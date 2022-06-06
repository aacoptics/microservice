package com.aacoptics.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Shao Xiang
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MS_00_CONTACT")
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long ID;

    private String USER_NO;

    private String USER_NAME;

    private String USER_EMAIL;

    private String USER_TEL;

    @TableField("CREATE_TIME")
    private LocalDateTime CREATE_TIME;

    @TableField("UPDATE_TIME")
    private LocalDateTime UPDATE_TIME;

}
