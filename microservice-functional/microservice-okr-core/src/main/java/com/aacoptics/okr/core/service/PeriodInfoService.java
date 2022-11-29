package com.aacoptics.okr.core.service;

import com.aacoptics.okr.core.entity.po.PeriodInfo;
import java.util.List;
public interface PeriodInfoService {
    boolean add(PeriodInfo periodInfo);

    boolean delete(Long id);

    boolean update(PeriodInfo periodInfo);

    List<PeriodInfo> listAll();

    PeriodInfo getById(Long id);
}