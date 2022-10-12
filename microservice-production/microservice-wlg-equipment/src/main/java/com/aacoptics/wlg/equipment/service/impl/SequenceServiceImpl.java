package com.aacoptics.wlg.equipment.service.impl;


import com.aacoptics.wlg.equipment.entity.po.MaintenanceOrderItem;
import com.aacoptics.wlg.equipment.entity.po.Sequence;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.mapper.SequenceMapper;
import com.aacoptics.wlg.equipment.service.SequenceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SequenceServiceImpl extends ServiceImpl<SequenceMapper, Sequence> implements SequenceService {

    @Resource
    SequenceMapper sequenceMapper;

    @Override
    public boolean add(Sequence sequence) {
        boolean isSuccess = this.save(sequence);
        if(isSuccess == false)
        {
            throw new BusinessException("新增维修序列号失败");
        }

        return isSuccess;
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(Sequence sequence) {

        boolean isSuccess = this.updateById(sequence);
        return isSuccess;
    }


    @Override
    public Sequence get(Long id) {
        Sequence sequence = this.getById(id);
        if (Objects.isNull(sequence)) {
            throw new BusinessException("ID为" + id + "的记录已不存在");
        }

        return sequence;
    }

    @Override
    public Long getNextNumberByName(String sequenceName) {

        QueryWrapper<Sequence> sequenceQueryWrapper = new QueryWrapper<Sequence>();
        sequenceQueryWrapper.eq("sequence_name", sequenceName);
        List<Sequence> sequenceList = sequenceMapper.selectList(sequenceQueryWrapper);
        if(sequenceList != null && sequenceList.size() > 0)
        {
            Sequence sequence = sequenceList.get(0);
            Long sequenceNumber = sequence.getSequenceNumber();
            Long incrementValue = sequence.getIncrementValue();

            Long nextSequenceNumber = sequenceNumber + incrementValue;
            sequence.setSequenceNumber(nextSequenceNumber);
            this.update(sequence);

            return nextSequenceNumber;

        }
        return null;
    }


    @Override
    public Sequence createSequence(String sequenceName, String description, Long initialValue, Long incrementValue, Long maxValue) {
        Sequence sequence = new Sequence();

        sequence.setSequenceName(sequenceName);
        sequence.setDescription(description);
        sequence.setInitialValue(initialValue);
        sequence.setIncrementValue(incrementValue);
        sequence.setMaxValue(maxValue);
        sequence.setSequenceNumber(initialValue);

        this.save(sequence);
        return sequence;
    }

}
