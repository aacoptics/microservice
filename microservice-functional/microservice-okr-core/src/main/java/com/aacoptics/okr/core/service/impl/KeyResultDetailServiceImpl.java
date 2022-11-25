package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;
import com.aacoptics.okr.core.mapper.KeyResultDetailMapper;
import com.aacoptics.okr.core.mapper.ObjectiveDetailMapper;
import com.aacoptics.okr.core.service.KeyResultDetailService;
import com.aacoptics.okr.core.service.ObjectiveDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KeyResultDetailServiceImpl extends ServiceImpl<KeyResultDetailMapper, KeyResultDetail> implements KeyResultDetailService {
    @Override
    public boolean add(KeyResultDetail keyResultDetail) {
        return this.save(keyResultDetail);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(KeyResultDetail keyResultDetail) {
        return this.updateById(keyResultDetail);
    }

    @Override
    public List<KeyResultDetail> listAllByOId(Long id) {
        QueryWrapper<KeyResultDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("objective_id", id)
                .eq("deleted", "N");
        return this.list(queryWrapper);
    }

    @Override
    public boolean updateStatusAndScore(KeyResultDetail keyResultDetail) {
        UpdateWrapper<KeyResultDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status_info", keyResultDetail.getStatusInfo())
                .set("score", keyResultDetail.getScore())
                .eq("id", keyResultDetail.getId());

        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteKeyResult(Long id) {
        QueryWrapper<KeyResultDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(KeyResultDetail::getId, id);
        return remove(queryWrapper);
    }

    @Override
    public boolean addOrUpdateKeyResult(KeyResultDetail keyResultDetail) {
        if(keyResultDetail.getId() != null)
            return this.updateById(keyResultDetail);
        else
            return this.add(keyResultDetail);
    }
}