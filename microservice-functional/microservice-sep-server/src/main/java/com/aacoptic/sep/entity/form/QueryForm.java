package com.aacoptic.sep.entity.form;

import com.aacoptic.sep.entity.vo.Group;
import lombok.Data;

import java.io.Serializable;


@Data
public class QueryForm implements Serializable {

    private String computerName;
    private String jobNumber;
    private String group;
}
