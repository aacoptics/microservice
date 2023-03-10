package com.aacoptics.feishu.photo.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AttendanceRecord implements Serializable {
    private String schema;

    private AttendanceRecordHeader header;

    private AttendanceRecordEvent event;
}
