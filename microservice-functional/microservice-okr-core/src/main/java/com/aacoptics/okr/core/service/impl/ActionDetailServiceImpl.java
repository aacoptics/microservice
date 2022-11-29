package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.ActionDetail;
import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.mapper.ActionDetailMapper;
import com.aacoptics.okr.core.mapper.KeyResultDetailMapper;
import com.aacoptics.okr.core.service.ActionDetailService;
import com.aacoptics.okr.core.service.KeyResultDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ActionDetailServiceImpl extends ServiceImpl<ActionDetailMapper, ActionDetail> implements ActionDetailService {
    @Override
    public boolean add(ActionDetail actionDetail) {
        return this.save(actionDetail);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ActionDetail actionDetail) {
        return this.updateById(actionDetail);
    }

    @Override
    public ActionDetail getById(Long id) {
        QueryWrapper<ActionDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("deleted", "N");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<ActionDetail> listAllByKrId(Long id) {
        QueryWrapper<ActionDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_result_id", id)
                .eq("deleted", "N");
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteAction(Long id) {
        QueryWrapper<ActionDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ActionDetail::getId, id);
        return remove(queryWrapper);
    }

    @Override
    public boolean addOrUpdateAction(ActionDetail actionDetail) {
        if (actionDetail.getId() != null)
            return this.updateById(actionDetail);
        else
            return this.add(actionDetail);
    }
}