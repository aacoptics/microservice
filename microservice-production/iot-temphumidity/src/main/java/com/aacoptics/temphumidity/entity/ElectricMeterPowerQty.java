package com.aacoptics.temphumidity.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class ElectricMeterPowerQty implements Serializable {
    private String workDate;

    private BigDecimal powerTotalQty;
}
