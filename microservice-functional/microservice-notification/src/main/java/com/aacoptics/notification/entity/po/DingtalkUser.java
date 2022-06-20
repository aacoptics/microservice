package com.aacoptics.notification.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Yan Shangqi
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("DINGTALK_USER")
public class DingtalkUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("USERID")
    @TableId(type = IdType.INPUT)
    private String userid;

    @TableField("NAME")
    private String name;

    @TableField("JOBNUMBER")
    private String jobNumber;

    @TableField("DEPTIDLIST")
    private String deptIdList;

    @TableField("EMAIL")
    private String email;

    @TableField("MOBILE")
    private String mobile;

    @TableField("TITLE")
    private String title;

    @TableField("WORKPLACE")
    private String workplace;

    @TableField("TELEPHONE")
    private String telephone;

    @TableField("TEL")
    private String tel;

    @TableField("ADACCOUNT")
    private String adAccount;

    @TableField("PERSK")
    private String persk;

    @TableField("PERSKCODE")
    private String perskCode;

    @TableField("COMPANY")
    private String company;

    @TableField("COMPANYCODE")
    private String companyCode;

    @TableField("ACTIVE")
    private Integer active;

    @TableField("TIMESTAMP")
    private Date timestamp;

    @TableField("MANAGERUSERID")
    private String managerUserid;
}
