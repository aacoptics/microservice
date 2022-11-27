package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.ObjectiveDetail;
import com.aacoptics.okr.core.entity.vo.TreeModel;

import java.util.List;

public interface ObjectiveDetailService {
    boolean add(ObjectiveDetail objectiveDetail);
    boolean delete(Long id);
    boolean update(ObjectiveDetail objectiveDetail);
    List<ObjectiveDetail> listAllByUsername(String username, Long periodId);

    List<ObjectiveDetail> listAllByUsername(String username, Long periodId, Long objectiveId, Boolean isAligned);

    ObjectiveDetail listById(Long id);

    boolean updateStatusAndScore(ObjectiveDetail objectiveDetail);

    boolean updateRemark(ObjectiveDetail objectiveDetail);

    boolean deleteObjective(Long id);

    boolean addOrUpdateObjective(ObjectiveDetail objectiveDetail);

    List<TreeModel> getUserObjectiveTree(String userInfo, Long periodId, String currentUsername, Long objectiveId);
}