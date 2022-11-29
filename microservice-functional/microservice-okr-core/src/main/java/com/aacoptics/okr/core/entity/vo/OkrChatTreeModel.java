package com.aacoptics.okr.core.entity.vo;

import com.aacoptics.okr.core.entity.po.KeyResultDetail;
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

    private String id;

    private String userName;

    private String labelName;

    private List<KeyResultDetail> keyResultDetails = new ArrayList<>();

    private List<OkrChatTreeModel> children = new ArrayList<>();

}
