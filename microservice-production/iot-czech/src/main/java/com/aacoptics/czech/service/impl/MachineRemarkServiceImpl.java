package com.aacoptics.czech.service.impl;

import com.aacoptics.czech.entity.MachineRemark;
import com.aacoptics.czech.mapper.MachineRemarkMapper;
import com.aacoptics.czech.service.MachineRemarkService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MachineRemarkServiceImpl extends ServiceImpl<MachineRemarkMapper, MachineRemark> implements MachineRemarkService {

    @Autowired
    MachineRemarkMapper machineRemarkMapper;

    @Transactional
    @Override
    public boolean saveRemark(String machineNumber, String content) {
        try {
            MachineRemark machineRemark = new MachineRemark();
            machineRemark.setMachineNumber(machineNumber).setContent(content).setCreateDate(new Date()).setIsDeleted(0);
            this.save(machineRemark);
            return true;
        }catch(Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public void updateRemark(MachineRemark machineRemark, String newContent) {
        UpdateWrapper<MachineRemark> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("content", newContent).eq("id", machineRemark.getId());
        update(updateWrapper);
    }

    @Override
    public void deleteRemark(MachineRemark machineRemark) {
        UpdateWrapper<MachineRemark> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_deleted", 1).eq("id", machineRemark.getId());
        update(updateWrapper);
    }

    @Override
    public List<Map<String, String>> getRemarkByMachineNumber(String machineNumber) {
        List<Map<String, String>> remarkList = machineRemarkMapper.getRemarkByMachineNumber(machineNumber);
        return remarkList;
    }
}
