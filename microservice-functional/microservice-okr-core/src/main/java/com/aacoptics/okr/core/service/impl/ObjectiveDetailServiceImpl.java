package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.*;
import com.aacoptics.okr.core.entity.vo.TreeModel;
import com.aacoptics.okr.core.mapper.ObjectiveDetailMapper;
import com.aacoptics.okr.core.mapper.PeriodInfoMapper;
import com.aacoptics.okr.core.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class ObjectiveDetailServiceImpl extends ServiceImpl<ObjectiveDetailMapper, ObjectiveDetail> implements ObjectiveDetailService {

    @Resource
    KeyResultDetailService keyResultDetailService;

    @Resource
    AlignRelationService alignRelationService;

    @Resource
    FeishuService feishuService;

    @Override
    public boolean add(ObjectiveDetail objectiveDetail) {
        return this.save(objectiveDetail);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean update(ObjectiveDetail objectiveDetail) {
        return this.updateById(objectiveDetail);
    }

    @Override
    public List<ObjectiveDetail> listAllByUsername(String username, Long periodId) {
        QueryWrapper<ObjectiveDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("created_by", username)
                .eq("period_id", periodId)
                .eq("deleted", "N");

        List<ObjectiveDetail> res = this.list(queryWrapper);

        for (ObjectiveDetail o : res) {
            o.setKeyResultDetails(keyResultDetailService.listAllByOId(o.getId()));
            o.setAlignRelations(alignRelationService.getAlignCountInfo(o.getId()));
        }
        return res;
    }

    @Override
    public List<ObjectiveDetail> listAllByUsername(String username, Long periodId, Long objectiveId) {
        List<AlignRelation> alignRelations = alignRelationService.listAllByOId(objectiveId);
        QueryWrapper<ObjectiveDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("created_by", username)
                .eq("period_id", periodId)
                .eq("deleted", "N");

        List<ObjectiveDetail> res = this.list(queryWrapper);

        for (ObjectiveDetail o : res) {
            if(alignRelations.stream().anyMatch(item -> item.getAlignType() == 2
                    && Objects.equals(item.getAlignId(), o.getId())))
                o.setAlreadyAlign(true);
            o.setKeyResultDetails(keyResultDetailService.listAllByOId(o.getId()));

            for (KeyResultDetail keyResultDetail : o.getKeyResultDetails()) {
                if(alignRelations.stream().anyMatch(item -> item.getAlignType() == 3
                        && Objects.equals(item.getAlignId(), keyResultDetail.getId())))
                    keyResultDetail.setAlreadyAlign(true);
            }
        }
        return res;
    }

    @Override
    public ObjectiveDetail listById(Long id) {
        QueryWrapper<ObjectiveDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("deleted", "N");

        ObjectiveDetail res = this.getOne(queryWrapper);

        if (res != null) {
            res.setKeyResultDetails(keyResultDetailService.listAllByOId(res.getId()));
        }
        return res;
    }

    @Override
    public boolean updateStatusAndScore(ObjectiveDetail objectiveDetail) {
        UpdateWrapper<ObjectiveDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status_info", objectiveDetail.getStatusInfo())
                .set("score", objectiveDetail.getScore())
                .eq("id", objectiveDetail.getId());

        return this.update(updateWrapper);
    }

    @Override
    public boolean updateRemark(ObjectiveDetail objectiveDetail) {
        UpdateWrapper<ObjectiveDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("remark", objectiveDetail.getRemark())
                .eq("id", objectiveDetail.getId());
        return this.update(updateWrapper);
    }

    @Override
    @Transactional
    public boolean deleteObjective(Long id) {
        ObjectiveDetail objectiveDetail = listById(id);
        if (objectiveDetail.getKeyResultDetails().size() > 0) {
            for (KeyResultDetail keyResultDetail : objectiveDetail.getKeyResultDetails()) {
                keyResultDetailService.deleteKeyResult(keyResultDetail.getId());
            }
        }
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean addOrUpdateObjective(ObjectiveDetail objectiveDetail) {
        if (objectiveDetail.getId() != null) {
            if (objectiveDetail.getKeyResultDetails().size() > 0) {
                for (KeyResultDetail keyResultDetail : objectiveDetail.getKeyResultDetails()) {
                    keyResultDetail.setObjectiveId(objectiveDetail.getId());
                    keyResultDetailService.addOrUpdateKeyResult(keyResultDetail);
                }
            }
        } else{
            this.add(objectiveDetail);
            if(objectiveDetail.getKeyResultDetails().size() > 0){
                for (KeyResultDetail keyResultDetail : objectiveDetail.getKeyResultDetails()) {
                    keyResultDetail.setObjectiveId(objectiveDetail.getId());
                    keyResultDetailService.addOrUpdateKeyResult(keyResultDetail);
                }
            }
        }

        return true;
    }

    @Override
    public List<TreeModel> getUserObjectiveTree(String userInfo, Long periodId, String currentUsername, Long objectiveId){
        List<FeishuUser> users = feishuService.getFeishuUsers(userInfo, currentUsername);
        List<TreeModel> res = new ArrayList<>();
        if(users.size() > 0){
            for (FeishuUser user : users) {
                TreeModel treeModel = new TreeModel();
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                treeModel.setId(uuid)
                        .setLabelName(user.getName())
                        .setRemark(user.getJobTitle())
                        .setExtendStr(user.getEmployeeNo())
                        .setNodeType(1);

                List<ObjectiveDetail> objectiveDetails = listAllByUsername(user.getEmployeeNo(), periodId);
                if(objectiveDetails.size() > 0){
                    List<TreeModel> objectiveRes = new ArrayList<>();
                    for (int i = 0; i < objectiveDetails.size(); i++) {
                        if(alignRelationService.checkAlignStatus(2, objectiveId, objectiveDetails.get(i).getId()))
                            continue;

                        TreeModel oTreeModel = new TreeModel();
                        String oUuid = UUID.randomUUID().toString().replaceAll("-","");
                        oTreeModel.setId(oUuid)
                                .setLabelName(objectiveDetails.get(i).getObjectiveName())
                                .setRemark(String.valueOf(i + 1))
                                .setAlignId(objectiveDetails.get(i).getId())
                                .setNodeType(2);

                        List<KeyResultDetail> keyResultDetails = keyResultDetailService.listAllByOId(objectiveDetails.get(i).getId());

                        if(keyResultDetails.size() > 0){
                            List<TreeModel> keyResultRes = new ArrayList<>();
                            for (int j = 0; j < keyResultDetails.size(); j++) {
                                if(alignRelationService.checkAlignStatus(3, objectiveId, keyResultDetails.get(j).getId()))
                                    continue;

                                TreeModel krTreeModel = new TreeModel();
                                String krUuid = UUID.randomUUID().toString().replaceAll("-","");
                                krTreeModel.setId(krUuid)
                                        .setLabelName(keyResultDetails.get(j).getKeyResultName())
                                        .setRemark(String.valueOf(j + 1))
                                        .setAlignId(keyResultDetails.get(j).getId())
                                        .setNodeType(3);
                                keyResultRes.add(krTreeModel);
                            }
                            oTreeModel.setChildren(keyResultRes);
                        }
                        objectiveRes.add(oTreeModel);
                    }
                    treeModel.setChildren(objectiveRes);
                    res.add(treeModel);
                }
            }
        }
        return res;
    }
}