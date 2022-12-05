package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.ProcessRecord;
import com.aacoptics.okr.core.mapper.ProcessRecordMapper;
import com.aacoptics.okr.core.service.ProcessRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {
    @Override
    public boolean add(ProcessRecord processRecord) {
        return this.save(processRecord);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ProcessRecord processRecord) {
        return this.updateById(processRecord);
    }

    @Override
    public List<ProcessRecord> listAllById(Long id, Integer type) {
        QueryWrapper<ProcessRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_type", type)
                .eq("parent_id", id);
        return this.list(queryWrapper);
    }

    @Override
    public boolean addOrUpdateAction(ProcessRecord processRecord) {
        if (processRecord.getId() != null)
            return this.updateById(processRecord);
        else
            return this.add(processRecord);
    }
}
