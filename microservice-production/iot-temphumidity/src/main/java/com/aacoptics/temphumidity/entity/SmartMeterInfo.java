package com.aacoptics.temphumidity.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SmartMeterInfo implements Serializable {
    private String deviceId;

    private String time;

    private String receiveDate;

    private String devTemplateUniqueId;

    private String thatMomentProgramVersionId;

    private String programVersion;

    private String clientId;

    private String dataColTime;

    private String cncStatus;

    private String uan;

    private String ubn;

    private String ucn;

    private String uab;

    private String ubc;

    private String uca;

    private String ia;

    private String ib;

    private String ic;

    private String pa;

    private String pb;

    private String pc;

    private String pTotal;

    private String qa;

    private String qb;

    private String qc;

    private String qTotal;

    private String sa;

    private String sb;

    private String sc;

    private String sTotal;

    private String fa;

    private String fb;

    private String fc;

    private String fTotal;

    private String epi;

    private String epe;

    private String eql;

    private String eqc;

    private String currentTr;

    private String voltageTr;
}
