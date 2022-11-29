package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.ActionDetail;
import com.aacoptics.okr.core.entity.po.ProcessRecord;
import io.swagger.models.auth.In;

import java.util.List;

public interface ProcessRecordService {
    boolean add(ProcessRecord processRecord);

    boolean delete(Long id);

    boolean update(ProcessRecord processRecord);

    List<ProcessRecord> listAllById(Long id, Integer type);
    boolean addOrUpdateAction(ProcessRecord processRecord);
}