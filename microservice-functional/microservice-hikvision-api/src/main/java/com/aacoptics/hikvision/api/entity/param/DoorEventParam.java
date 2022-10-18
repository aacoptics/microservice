package com.aacoptics.hikvision.api.entity.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorEventParam implements Serializable {

    @JSONField(format="yyyy-MM-dd'T'HH:mm:ss'+08:00'")
    private LocalDateTime startTime;

    @JSONField(format="yyyy-MM-dd'T'HH:mm:ss'+08:00'")
    private LocalDateTime endTime;
    private Integer pageNo;
    private Integer pageSize = 1000;
}
