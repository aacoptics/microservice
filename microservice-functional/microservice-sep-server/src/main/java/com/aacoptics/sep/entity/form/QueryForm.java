package com.aacoptics.sep.entity.form;

import lombok.Data;

import java.io.Serializable;


@Data
public class QueryForm implements Serializable {

    private String computerName;
    private String jobNumber;
    private String group;
}
