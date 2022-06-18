package com.aacoptics.sep.entity.form;

import com.aacoptics.sep.entity.vo.Group;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChangeForm implements Serializable {

    private Group group;
    private String hardwareKey;
}
