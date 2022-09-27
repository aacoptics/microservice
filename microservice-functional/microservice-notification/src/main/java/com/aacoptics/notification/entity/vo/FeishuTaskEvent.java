package com.aacoptics.notification.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class FeishuTaskEvent implements Serializable {
    private String schema;

    private Header header;

    private Event event;

    @Data
    @NoArgsConstructor
    public class Event implements Serializable{
        private String task_id;

        private String comment_id;

        private String parent_id;

        private int obj_type;
    }

    @Data
    @NoArgsConstructor
    public class Header implements Serializable{
        private String event_id;

        private String event_type;

        private Long create_time;

        private String token;

        private String app_id;

        private String tenant_key;
    }
}