package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;

import java.util.List;

public interface KeyResultDetailService {
    boolean add(KeyResultDetail keyResultDetail);

    boolean delete(Long id);

    boolean update(KeyResultDetail keyResultDetail);

    List<KeyResultDetail> listAllByOId(Long id);

    KeyResultDetail listById(Long id);
    String getMarkDownMessage(KeyResultDetail keyResultDetail, String periodName);

    boolean updateStatusAndScore(KeyResultDetail keyResultDetail);

    boolean deleteKeyResult(Long id);

    boolean addOrUpdateKeyResult(KeyResultDetail keyResultDetail, String periodName);
}