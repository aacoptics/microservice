package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.AlignRelation;
import com.aacoptics.okr.core.entity.po.PeriodInfo;

import java.util.List;

public interface AlignRelationService {
    boolean add(AlignRelation alignRelation);

    boolean checkCycleAlign(AlignRelation alignRelation, Long alignId);

    boolean deleteAlignInfo(Integer alignType, Long ObjectiveId, Long alignId);

    boolean deleteAlignInfo(Long ObjectiveId);

    boolean update(AlignRelation alignRelation);

    boolean checkAlignStatus(Integer alignType, Long ObjectiveId, Long alignId);

    List<AlignRelation> listAllByOId(Long id);

    List<AlignRelation> listAlignedByOId(Long id);

    List<AlignRelation> getAlignCountInfo(Long id);

    List<AlignRelation> getAlignedCountInfo(Long id);
}