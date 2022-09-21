package com.aacoptics.notification.entity.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class FeishuTaskVo implements Serializable {
        private boolean can_edit;
        private List<UserId> collaborators;
        private String complete_time;
        private String create_time;
        private String creator_id;
        private String custom;
        private String description;
        private Due due;
        private String extra;
        private List<UserId> followers;
        private String id;
        private Origin origin;
        private String repeat_rule;
        private String rich_description;
        private String rich_summary;
        private int source;
        private String summary;
        private String update_time;

    @Data
    @NoArgsConstructor
    public class UserId implements Serializable {
        private String id;
    }

    @Data
    @NoArgsConstructor
    public class Due implements Serializable {
        private boolean is_all_day;
        private long time;
        private String timezone;
    }


    @Data
    @NoArgsConstructor
    public class Href implements Serializable {
        private String title;
        private String url;
    }

    @Data
    @NoArgsConstructor
    public class Origin implements Serializable {
        private Href href;
        private String platform_i18n_name;
    }
}


