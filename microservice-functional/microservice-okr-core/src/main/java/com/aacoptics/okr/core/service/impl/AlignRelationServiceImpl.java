package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.AlignRelation;
import com.aacoptics.okr.core.exception.BusinessException;
import com.aacoptics.okr.core.exception.CommonErrorType;
import com.aacoptics.okr.core.mapper.AlignRelationMapper;
import com.aacoptics.okr.core.service.AlignRelationService;
import com.aacoptics.okr.core.service.KeyResultDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AlignRelationServiceImpl extends ServiceImpl<AlignRelationMapper, AlignRelation> implements AlignRelationService {

    @Resource
    AlignRelationMapper alignRelationMapper;

    @Resource
    KeyResultDetailService keyResultDetailService;

    @Override
    public boolean add(AlignRelation alignRelation) {
        if (checkCycleAlign(alignRelation, alignRelation.getAlignId()))
            return this.save(alignRelation);
        else
            throw new BusinessException(CommonErrorType.ALIGN_CYCLE_EXCEPTION);
    }

    public boolean checkCycleAlign(AlignRelation alignRelation, Long alignId) {
        Long myObjectiveId = alignRelation.getObjectiveId();
        List<AlignRelation> alignRelations = listAlignedByOId(myObjectiveId);
        if (alignRelations.size() > 0) {
            for (AlignRelation relation : alignRelations) {
                if (Objects.equals(relation.getObjectiveId(), alignId))
                    return false;

                if (!keyResultDetailService.checkValid(alignId, relation.getObjectiveId()))
                    return false;

                if (!checkCycleAlign(relation, alignId))
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteAlignInfo(Integer alignType, Long ObjectiveId, Long alignId) {
        QueryWrapper<AlignRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("align_type", alignType)
                .eq("align_id", alignId)
                .eq("objective_id", ObjectiveId);
        return remove(queryWrapper);
    }

    @Override
    public boolean deleteAlignInfo(Long ObjectiveId) {
        QueryWrapper<AlignRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("align_id", ObjectiveId)
                .or().eq("objective_id", ObjectiveId);
        return remove(queryWrapper);
    }

    @Override
    public boolean update(AlignRelation alignRelation) {
        return this.updateById(alignRelation);
    }

    @Override
    public boolean checkAlignStatus(Integer alignType, Long ObjectiveId, Long alignId) {
        QueryWrapper<AlignRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("align_type", alignType)
                .eq("align_id", alignId)
                .eq("objective_id", ObjectiveId);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<AlignRelation> listAllByOId(Long id) {
        QueryWrapper<AlignRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("objective_id", id);
        return this.list(queryWrapper);
    }

    @Override
    public List<AlignRelation> listAlignedByOId(Long id) {
        return alignRelationMapper.listAlignedByOId(id);
    }

    @Override
    public List<AlignRelation> getAlignCountInfo(Long id) {
        return alignRelationMapper.getAlignCountInfo(id);
    }

    @Override
    public List<AlignRelation> getAlignedCountInfo(Long id) {
        return alignRelationMapper.getAlignedCountInfo(id);
    }
}
