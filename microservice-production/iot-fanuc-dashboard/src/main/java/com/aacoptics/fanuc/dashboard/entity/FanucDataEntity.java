package com.aacoptics.fanuc.dashboard.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FanucDataEntity implements Serializable {
    private Object monitData;

    private Object condData;

    private Object moldData;
}
