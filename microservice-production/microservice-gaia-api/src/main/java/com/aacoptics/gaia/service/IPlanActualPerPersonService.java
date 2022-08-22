package com.aacoptics.gaia.service;

import com.aacoptics.gaia.entity.form.PlanActualPerPersonForm;
import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.aacoptics.gaia.entity.vo.MessageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IPlanActualPerPersonService extends IService<PlanActualPerPerson> {

    String RECEIVE_ID_TYPE_OPEN_ID = "open_id";
    String RECEIVE_ID_TYPE_USER_ID = "user_id";
    String RECEIVE_ID_TYPE_EMAIL = "email";
    String RECEIVE_ID_TYPE_CHAT_ID = "chat_id";

    List<PlanActualPerPerson> getPlanInfoByTime(LocalDateTime startDate, LocalDateTime endDate);

    void updatePlanResult(List<PlanActualPerPersonForm> planActualPerPersons);

    List<MessageInfo> getPersonPlanMsg();

    List<PlanActualPerPerson> getDataByWorkDate(String workDate);

    List<PlanActualPerPerson> getWorkDate();

    void sendFeishuMessage();
}
