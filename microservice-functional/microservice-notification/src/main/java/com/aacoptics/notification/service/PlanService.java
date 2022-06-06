package com.aacoptics.notification.service;


import java.util.List;
import java.util.Map;

public interface PlanService {

    String insertPlanData(Map<String, String> params)  throws Exception ;

    String insertPlanContact(String planKey, String[] contactArray)  throws Exception ;

    String updatePlanData(Map<String, String> params)  throws Exception ;

    List<Map<String, Object>> filterPlanData(String planKey, String planName);

    List<Map<String, Object>> filterPlanJOB(String planKey, String planName, String workDay);

    List<Map<String, Object>> filterContactData(String userName);

    List<Map<String, Object>> filterPlanContact(String planKey);

    List<Map<String, Object>> queryScheduledPlan();

    String updateUser();
}
