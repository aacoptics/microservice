package com.aacoptics.mold.toollife.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("INV_TOOL_SCRAP_DETAIL")
public class ScrapedTool implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codeNo;

    private String matCode;

    private String matName;

    private String lifeSalvage;

    private String createDate;




}
