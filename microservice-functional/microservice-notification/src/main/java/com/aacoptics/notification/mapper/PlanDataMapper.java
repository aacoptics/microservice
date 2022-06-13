package com.aacoptics.notification.mapper;

import com.aacoptics.notification.entity.po.PlanData;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface PlanDataMapper extends BaseMapper<PlanData> {

    @DS("msg_db")
    void insertPlanData(Map<String, Object> param);

    @DS("msg_db")
    void insertPlanContact(Map<String, Object> param);

    @DS("msg_db")
    void insertContactData(Map<String, Object> param);

    @DS("msg_db")
    void insertPlanJob(Map<String, Object> param);

    @DS("msg_db")
    void deletePlanContact(String planKey);

    @DS("msg_db")
    void deleteContactData(String USER_NO);

    @DS("msg_db")
    void updatePlanData(Map<String, Object> param);

    @DS("msg_db")
    List<Map<String, Object>> filterPlanData(Map<String, Object> param);

    @DS("msg_db")
    List<Map<String, Object>> filterPlanJOB(Map<String, Object> param);

    @DS("msg_db")
    List<Map<String, Object>> filterContactData(Map<String, Object> param);

    @DS("msg_db")
    List<Map<String, Object>> filterPlanContact(Map<String, Object> param);

    @DS("msg_db")
    List<Map<String, Object>> queryScheduledPlan(Map<String, Object> param);
}