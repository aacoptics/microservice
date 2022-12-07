package com.aacoptics.okr.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aacoptics.okr.core.entity.po.*;
import com.aacoptics.okr.core.entity.vo.MarkdownGroupMessage;
import com.aacoptics.okr.core.entity.vo.OkrChatTreeModel;
import com.aacoptics.okr.core.entity.vo.TreeModel;
import com.aacoptics.okr.core.mapper.ObjectiveDetailMapper;
import com.aacoptics.okr.core.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ObjectiveDetailServiceImpl extends ServiceImpl<ObjectiveDetailMapper, ObjectiveDetail> implements ObjectiveDetailService {

    @Resource
    ObjectiveDetailMapper mapper;

    @Resource
    KeyResultDetailService keyResultDetailService;

    @Resource
    PeriodInfoService periodInfoService;

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
            o.setAlignedRelations(alignRelationService.getAlignedCountInfo(o.getId()));
        }
        return res;
    }

    @Override
    public List<ObjectiveDetail> listAllByUsername(String username, Long periodId, Long objectiveId, Boolean isAligned) {
        QueryWrapper<ObjectiveDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("created_by", username)
                .eq("period_id", periodId)
                .eq("deleted", "N");

        List<ObjectiveDetail> res = this.list(queryWrapper);
        List<AlignRelation> alignRelations;
        if (isAligned) {
            alignRelations = alignRelationService.listAlignedByOId(objectiveId);
        } else {
            alignRelations = alignRelationService.listAllByOId(objectiveId);
        }

        for (ObjectiveDetail o : res) {
            if (isAligned) {
                if (alignRelations.stream().anyMatch(item -> item.getAlignType() == 2
                        && Objects.equals(item.getObjectiveId(), o.getId())))
                    o.setAlreadyAlign(true);
                o.setKeyResultDetails(keyResultDetailService.listAllByOId(o.getId()));
            } else {
                if (alignRelations.stream().anyMatch(item -> item.getAlignType() == 2
                        && Objects.equals(item.getAlignId(), o.getId())))
                    o.setAlreadyAlign(true);
                o.setKeyResultDetails(keyResultDetailService.listAllByOId(o.getId()));
            }

            for (KeyResultDetail keyResultDetail : o.getKeyResultDetails()) {
                if (isAligned) {
                    if (alignRelations.stream().anyMatch(item -> item.getAlignType() == 3
                            && Objects.equals(item.getObjectiveId(), keyResultDetail.getId())))
                        keyResultDetail.setAlreadyAlign(true);
                } else {
                    if (alignRelations.stream().anyMatch(item -> item.getAlignType() == 3
                            && Objects.equals(item.getAlignId(), keyResultDetail.getId())))
                        keyResultDetail.setAlreadyAlign(true);
                }
            }
        }
        return res;
    }

    @Override
    public ObjectiveDetail listById(Long id) {
        ObjectiveDetail res = getOneById(id);

        if (res != null) {
            res.setKeyResultDetails(keyResultDetailService.listAllByOId(res.getId()));
        }
        return res;
    }

    @Override
    public ObjectiveDetail getOneById(Long id) {
        QueryWrapper<ObjectiveDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("deleted", "N");
        return this.getOne(queryWrapper);
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
        alignRelationService.deleteAlignInfo(id);
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean addOrUpdateObjective(ObjectiveDetail objectiveDetail) {
        PeriodInfo periodInfo = periodInfoService.getById(objectiveDetail.getPeriodId());

        if (periodInfo == null) {
            log.error("找不到该周期！");
        }
        if (objectiveDetail.getId() != null) {
            ObjectiveDetail previousObjectiveDetail = getOneById(objectiveDetail.getId());
            String previousAtUsers = previousObjectiveDetail.getAtUsers();
            this.updateById(objectiveDetail);
            if (objectiveDetail.getUsers() != null && objectiveDetail.getUsers().size() > 0 && periodInfo != null) {
                for (FeishuUser user : objectiveDetail.getUsers()) {
                    if (previousAtUsers == null || !previousAtUsers.contains(user.getEmployeeNo()))
                        feishuService.sendPersonalMessage(user, feishuService.getMarkdownMessage(getMarkDownMessage(objectiveDetail, periodInfo.getPeriodName()), null));
                }
            }
        } else {
            this.add(objectiveDetail);
            if (objectiveDetail.getUsers() != null && objectiveDetail.getUsers().size() > 0 && periodInfo != null) {
                for (FeishuUser user : objectiveDetail.getUsers()) {
                    feishuService.sendPersonalMessage(user, feishuService.getMarkdownMessage(getMarkDownMessage(objectiveDetail, periodInfo.getPeriodName()), null));
                }
            }
        }

        if (objectiveDetail.getKeyResultDetails().size() > 0) {
            for (KeyResultDetail keyResultDetail : objectiveDetail.getKeyResultDetails()) {
                if (StrUtil.isBlank(keyResultDetail.getKeyResultName()))
                    continue;
                keyResultDetail.setObjectiveId(objectiveDetail.getId());
                keyResultDetailService.addOrUpdateKeyResult(objectiveDetail, keyResultDetail, periodInfo.getPeriodName());
            }
        }
        return true;
    }


    @Override
    public String getMarkDownMessage(ObjectiveDetail objectiveDetail, String periodName) {
        MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
        markdownGroupMessage.setTitle("有一条Objective提及到您：");
        markdownGroupMessage.addBlobContent("周期：" + periodName);
        markdownGroupMessage.addContent("Objective内容：" + objectiveDetail.getObjectiveName());
        String atUsers = objectiveDetail.getUsers().stream().map(FeishuUser::getName).collect(Collectors.joining(","));
        markdownGroupMessage.addContent("提及人员：" + atUsers);
        markdownGroupMessage.addContent(StrUtil.format("[查看详情](https://open.feishu.cn/open-apis/authen/v1/index?app_id=cli_a3f634b596a3100c&redirect_uri=http://udsapi.aacoptics.com/okrAtUser?username={}&objectiveDetailId={})",
                objectiveDetail.getCreatedBy(), objectiveDetail.getId()));
        return markdownGroupMessage.toString();
    }


    @Override
    public List<TreeModel> getUserObjectiveTree(String userInfo, Long periodId, String currentUsername, Long objectiveId) {
        List<FeishuUser> users = feishuService.getFeishuUsers(userInfo, currentUsername);
        List<TreeModel> res = new ArrayList<>();
        if (users.size() > 0) {
            for (FeishuUser user : users) {
                TreeModel treeModel = new TreeModel();
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                treeModel.setId(uuid)
                        .setLabelName(user.getName())
                        .setRemark(user.getJobTitle())
                        .setExtendStr(user.getEmployeeNo())
                        .setNodeType(1);

                List<ObjectiveDetail> objectiveDetails = listAllByUsername(user.getEmployeeNo(), periodId);
                if (objectiveDetails.size() > 0) {
                    List<TreeModel> objectiveRes = new ArrayList<>();
                    for (int i = 0; i < objectiveDetails.size(); i++) {
                        if (alignRelationService.checkAlignStatus(2, objectiveId, objectiveDetails.get(i).getId()))
                            continue;

                        TreeModel oTreeModel = new TreeModel();
                        String oUuid = UUID.randomUUID().toString().replaceAll("-", "");
                        oTreeModel.setId(oUuid)
                                .setLabelName(objectiveDetails.get(i).getObjectiveName())
                                .setRemark(String.valueOf(i + 1))
                                .setAlignId(objectiveDetails.get(i).getId())
                                .setNodeType(2);

                        List<KeyResultDetail> keyResultDetails = keyResultDetailService.listAllByOId(objectiveDetails.get(i).getId());

                        if (keyResultDetails.size() > 0) {
                            List<TreeModel> keyResultRes = new ArrayList<>();
                            for (int j = 0; j < keyResultDetails.size(); j++) {
                                if (alignRelationService.checkAlignStatus(3, objectiveId, keyResultDetails.get(j).getId()))
                                    continue;

                                TreeModel krTreeModel = new TreeModel();
                                String krUuid = UUID.randomUUID().toString().replaceAll("-", "");
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

    @Override
    public List<Tuple2<List<OkrChatTreeModel>, List<OkrChatTreeModel>>> okrAlignChat(String employNo, Long periodId) {
        List<Tuple2<List<OkrChatTreeModel>, List<OkrChatTreeModel>>> res = new ArrayList<>();
        if (StrUtil.isEmpty(employNo) || periodId == null || periodId <= 0) return res;
        final FeishuUser feishuUser = feishuService.getFeishuUser(employNo);
        if (ObjectUtil.isNull(feishuUser)) return res;

        final List<ObjectiveDetail> objectiveDetails = mapper.selectByEmployNoAndPeriod(employNo, periodId);
        for (ObjectiveDetail objectiveDetail : objectiveDetails) {
            if (CollUtil.isEmpty(objectiveDetail.getAlignRelations()) && CollUtil.isEmpty(objectiveDetail.getAlignedRelations()))
                continue;
            OkrChatTreeModel alignOkrChatTreeModel = new OkrChatTreeModel();
            alignOkrChatTreeModel.setId(objectiveDetail.getId())
                    .setLabel(feishuUser.getName())
                    .setContent(objectiveDetail.getObjectiveName())
                    .setKeyResultDetails(objectiveDetail.getKeyResultDetails().stream().sorted(Comparator.comparing(KeyResultDetail::getCreatedTime)).collect(Collectors.toList()))
                    .setChildren(alignChildren(objectiveDetail.getAlignRelations()));
            List<OkrChatTreeModel> alignOkrChatTreeModels = new ArrayList<>();
            alignOkrChatTreeModels.add(alignOkrChatTreeModel);
            OkrChatTreeModel alignedOkrChatTreeModel = new OkrChatTreeModel();
            alignedOkrChatTreeModel.setId(objectiveDetail.getId())
                    .setLabel(feishuUser.getName())
                    .setContent(objectiveDetail.getObjectiveName())
                    .setKeyResultDetails(objectiveDetail.getKeyResultDetails().stream().sorted(Comparator.comparing(KeyResultDetail::getCreatedTime)).collect(Collectors.toList()))
                    .setChildren(alignedChildren(objectiveDetail.getAlignedRelations()));
            List<OkrChatTreeModel> alignedOkrChatTreeModels = new ArrayList<>();
            alignedOkrChatTreeModels.add(alignedOkrChatTreeModel);
            res.add(Tuples.of(alignOkrChatTreeModels, alignedOkrChatTreeModels));
        }

        IntStream.range(0, res.size()).forEach(i -> {
            res.get(i).getT1().get(0).setIndex(i + 1);
            res.get(i).getT2().get(0).setIndex(i + 1);
        });
        return res;
    }

    private List<OkrChatTreeModel> alignChildren(List<AlignRelation> alignRelations) {
        List<OkrChatTreeModel> res = new ArrayList<>();
        if (CollUtil.isEmpty(alignRelations)) return res;
        alignRelations.stream().sorted(Comparator.comparing(AlignRelation::getCreatedTime)).forEach(alignRelation -> {
            OkrChatTreeModel okrChatTreeModel = new OkrChatTreeModel();
            ObjectiveDetail objectiveDetail = alignRelation.getAlignType() == 2
                    ? mapper.listAlignByOId(alignRelation.getAlignId())
                    : mapper.listAlignByKId(alignRelation.getAlignId());
            if (ObjectUtil.isNull(objectiveDetail)) return;
            okrChatTreeModel.setId(objectiveDetail.getId())
                    .setLabel(alignRelation.getOwnerRealName())
                    .setContent(objectiveDetail.getObjectiveName())
                    .setKeyResultDetails(objectiveDetail.getKeyResultDetails())
                    .setIndex(1)
                    .setChildren(alignChildren(objectiveDetail.getAlignRelations()));
            res.add(okrChatTreeModel);
        });
        return res;
    }

    private List<OkrChatTreeModel> alignedChildren(List<AlignRelation> alignedRelations) {
        List<OkrChatTreeModel> res = new ArrayList<>();
        if (CollUtil.isEmpty(alignedRelations)) return res;
        alignedRelations.stream().sorted(Comparator.comparing(AlignRelation::getCreatedTime)).forEach(alignedRelation -> {
            OkrChatTreeModel okrChatTreeModel = new OkrChatTreeModel();
            ObjectiveDetail objectiveDetail = mapper.listAlignedByOId(alignedRelation.getObjectiveId());
            if (ObjectUtil.isNull(objectiveDetail)) return;
            okrChatTreeModel.setId(objectiveDetail.getId())
                    .setLabel(alignedRelation.getObjectiveRealName())
                    .setContent(objectiveDetail.getObjectiveName())
                    .setKeyResultDetails(objectiveDetail.getKeyResultDetails().stream().sorted(Comparator.comparing(KeyResultDetail::getCreatedTime)).collect(Collectors.toList()))
                    .setIndex(1)
                    .setChildren(alignedChildren(objectiveDetail.getAlignedRelations()));
            res.add(okrChatTreeModel);
        });
        return res;
    }
}
