package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.ActionDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;

import java.util.List;

public interface ActionDetailService {
    boolean add(ActionDetail actionDetail);

    boolean delete(Long id);

    boolean update(ActionDetail actionDetail);

    ActionDetail getById(Long id);

    List<ActionDetail> listAllByKrId(Long id);

    boolean deleteAction(Long id);

    String getMarkDownMessage(ObjectiveDetail objectiveDetail, ActionDetail actionDetail);

    String getMarkDownResourceMessage(ObjectiveDetail objectiveDetail, ActionDetail actionDetail);

    boolean addOrUpdateAction(ActionDetail actionDetail);
}
