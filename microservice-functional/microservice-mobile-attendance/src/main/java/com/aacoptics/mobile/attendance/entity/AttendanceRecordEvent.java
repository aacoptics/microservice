package com.aacoptics.mobile.attendance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AttendanceRecordEvent implements Serializable {
    private String employee_id;

    private String employee_no;

    private String location_name;

    private String check_time;

    private String comment;

    private String record_id;

    private Float longitude;

    private Float latitude;

    private String ssid;

    private String bssid;

    private Boolean is_field;

    private Boolean is_wifi;

    private Integer type;

    private List<String> photo_urls;
}