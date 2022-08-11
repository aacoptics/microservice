package com.aacoptics.mobile.attendance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AttendanceRecordHeader implements Serializable {
    private String event_id;

    private String event_type;

    private String create_time;

    private String token;

    private String app_id;

    private String tenant_key;
}