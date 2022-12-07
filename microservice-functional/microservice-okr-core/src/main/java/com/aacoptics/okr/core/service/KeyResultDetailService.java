package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;

import java.util.List;

public interface KeyResultDetailService {
    boolean add(KeyResultDetail keyResultDetail);

    boolean delete(Long id);

    boolean update(KeyResultDetail keyResultDetail);

    List<KeyResultDetail> listAllByOId(Long id);

    boolean checkValid(Long id, Long ObjectiveId);

    KeyResultDetail listById(Long id);

    String getMarkDownMessage(ObjectiveDetail objectiveDetail, KeyResultDetail keyResultDetail, String periodName);

    boolean updateStatusAndScore(KeyResultDetail keyResultDetail);

    boolean deleteKeyResult(Long id);

    boolean addOrUpdateKeyResult(ObjectiveDetail objectiveDetail, KeyResultDetail keyResultDetail, String periodName);
}
