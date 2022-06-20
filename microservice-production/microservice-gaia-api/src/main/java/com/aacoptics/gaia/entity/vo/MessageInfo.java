package com.aacoptics.gaia.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class MessageInfo implements Serializable {

    private String JobNumber;

    private String Message;

    private List<Integer> idList;
}
