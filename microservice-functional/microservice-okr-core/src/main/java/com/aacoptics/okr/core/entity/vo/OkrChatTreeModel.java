package com.aacoptics.okr.core.entity.vo;

import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kaizhi Xuan
 * created on 2022/11/29 15:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OkrChatTreeModel implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String label;

    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer index;

    private List<KeyResultDetail> keyResultDetails = new ArrayList<>();

    private List<OkrChatTreeModel> children = new ArrayList<>();

}
