package com.aacoptics.wlg.equipment.entity.po;

import com.aacoptics.common.web.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("em_image")
public class Image extends BasePo {

    /**
     * 文件名
     */
    @TableField(value = "file_name")
    private String fileName;


    /**
     * 文件扩展
     */
    @TableField(value = "file_extension")
    private String fileExtension;


    /**
     * 图片
     */
    @TableField(value = "image")
    private byte[] image;


    /**
     * 宽
     */
    @TableField(value = "width")
    private Integer width;

    /**
     * 高
     */
    @TableField(value = "height")
    private Integer height;


}
