package com.aacoptic.sep.entity.form;

import com.aacoptic.sep.entity.vo.Group;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChangeForm implements Serializable {

    private Group group;
    private String hardwareKey;
}
