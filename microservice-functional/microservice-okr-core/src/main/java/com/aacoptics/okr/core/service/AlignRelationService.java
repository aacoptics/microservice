package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.AlignRelation;
import com.aacoptics.okr.core.entity.po.PeriodInfo;

import java.util.List;

public interface AlignRelationService {
    boolean add(AlignRelation alignRelation);

    boolean deleteAlignInfo(Integer alignType, Long ObjectiveId, Long alignId);

    boolean update(AlignRelation alignRelation);

    boolean checkAlignStatus(Integer alignType, Long ObjectiveId, Long alignId);

    List<AlignRelation> listAllByOId(Long id);

    List<AlignRelation> getAlignCountInfo(Long id);
}