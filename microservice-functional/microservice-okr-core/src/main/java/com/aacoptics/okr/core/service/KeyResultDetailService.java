package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;

import java.util.List;

public interface KeyResultDetailService {
    boolean add(KeyResultDetail keyResultDetail);

    boolean delete(Long id);

    boolean update(KeyResultDetail keyResultDetail);

    List<KeyResultDetail> listAllByOId(Long id);

    boolean updateStatusAndScore(KeyResultDetail keyResultDetail);

    boolean deleteKeyResult(Long id);

    boolean addOrUpdateKeyResult(KeyResultDetail keyResultDetail);
}