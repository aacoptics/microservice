package com.aacoptics.gaia.service;

import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IPlanActualPerPersonService extends IService<PlanActualPerPerson> {

    List<PlanActualPerPerson> getPlanInfoByTime(LocalDateTime startDate, LocalDateTime endDate);

    void updatePlanResult(List<PlanActualPerPerson> planActualPerPersons);
}
