package com.aacoptics.czech.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class StatusInfo implements Serializable {
    private String status;

    private String time;

}
