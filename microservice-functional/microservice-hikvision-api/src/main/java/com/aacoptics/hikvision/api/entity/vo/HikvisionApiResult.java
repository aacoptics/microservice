package com.aacoptics.hikvision.api.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HikvisionApiResult<T> implements Serializable {

    private String code;

    private String msg;

    private T data;
}
