package com.aacoptics.coating.dashboard.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CoatingStatusEntity implements Serializable {

    private boolean IsOnline;

    private String Name;

    private Integer RunNo;

    private Integer Status;

    private Integer TotalNums;
}