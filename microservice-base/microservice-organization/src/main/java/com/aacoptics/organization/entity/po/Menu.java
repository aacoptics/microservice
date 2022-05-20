package com.aacoptics.organization.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("menu")
public class Menu extends BasePo {

    /**
     * 目录
     */
    public static final int MENU_TYPE_CATALOG = 0;

    /**
     * 菜单
     */
    public static final int MENU_TYPE_MENU = 1;

    /**
     * 外链
     */
    public static final int MENU_TYPE_LINK = 2;

    private Long parentId;
    private String name;
    private String path;
    private String component;
    private String title;
    private String icon;
    private Integer orderNum;
    private String description;
    private String webUrl;
    private Boolean visible = true;
    private Integer menuType = 1;
    @TableField(exist = false)
    private Set<Menu> children;
    @TableField(exist = false)
    private String roles;

}