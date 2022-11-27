package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.AlignRelation;
import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;
import com.aacoptics.okr.core.entity.po.PeriodInfo;
import com.aacoptics.okr.core.mapper.AlignRelationMapper;
import com.aacoptics.okr.core.mapper.PeriodInfoMapper;
import com.aacoptics.okr.core.service.AlignRelationService;
import com.aacoptics.okr.core.service.PeriodInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class AlignRelationServiceImpl extends ServiceImpl<AlignRelationMapper, AlignRelation> implements AlignRelationService {

    @Resource
    AlignRelationMapper alignRelationMapper;

    @Override
    public boolean add(AlignRelation alignRelation) {
        return this.save(alignRelation);
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
        QueryWrapper<AlignRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("align_id", id);
        return this.list(queryWrapper);
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