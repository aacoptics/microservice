package com.aacoptics.gaia.service.impl;

import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.aacoptics.gaia.mapper.PlanActualPerPersonMapper;
import com.aacoptics.gaia.service.IPlanActualPerPersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PlanActualPerPersonService extends ServiceImpl<PlanActualPerPersonMapper, PlanActualPerPerson> implements IPlanActualPerPersonService {

    @Resource
    PlanActualPerPersonMapper planActualPerPersonMapper;

    @Override
    public List<PlanActualPerPerson> getPlanInfoByTime(LocalDateTime startDate, LocalDateTime endDate) {
        return planActualPerPersonMapper.getPlanInfoByTime(startDate, endDate);
    }

    public void updatePlanResult(List<PlanActualPerPerson> planActualPerPersons){
        for (PlanActualPerPerson planActualPerPerson : planActualPerPersons) {
            UpdateWrapper<PlanActualPerPerson> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("ID", planActualPerPerson.getId());
            updateWrapper.set("gy_transfer_flg",  planActualPerPerson.getGyTransferFlg());
            updateWrapper.set("transfer_err",  planActualPerPerson.getTransferErr());
            baseMapper.update(null, updateWrapper);
        }
    }
}
