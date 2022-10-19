package com.aacoptics.hikvision.api.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDetail implements Serializable {

    private String personName;

    private String jobNo;
}
