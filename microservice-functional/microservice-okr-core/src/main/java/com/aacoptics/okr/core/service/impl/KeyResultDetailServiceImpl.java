package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.aacoptics.okr.core.entity.po.KeyResultDetail;
import com.aacoptics.okr.core.entity.po.ObjectiveDetail;
import com.aacoptics.okr.core.entity.po.PeriodInfo;
import com.aacoptics.okr.core.entity.vo.MarkdownGroupMessage;
import com.aacoptics.okr.core.mapper.KeyResultDetailMapper;
import com.aacoptics.okr.core.mapper.ObjectiveDetailMapper;
import com.aacoptics.okr.core.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeyResultDetailServiceImpl extends ServiceImpl<KeyResultDetailMapper, KeyResultDetail> implements KeyResultDetailService {

    @Resource
    ActionDetailService actionDetailService;

    @Resource
    AlignRelationService alignRelationService;

    @Resource
    FeishuService feishuService;

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
        List<KeyResultDetail> res = this.list(queryWrapper);
        for (KeyResultDetail o : res) {
            o.setActionDetails(actionDetailService.listAllByKrId(o.getId()));
        }
        return res;
    }

    @Override
    public KeyResultDetail listById(Long id) {
        QueryWrapper<KeyResultDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("deleted", "N");
        return this.getOne(queryWrapper);
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
        alignRelationService.deleteAlignInfo(id);
        QueryWrapper<KeyResultDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(KeyResultDetail::getId, id);
        return remove(queryWrapper);
    }

    @Override
    public boolean addOrUpdateKeyResult(KeyResultDetail keyResultDetail, String periodName) {
        if (keyResultDetail.getId() != null) {
            KeyResultDetail previousKeyResultDetail = listById(keyResultDetail.getId());
            String previousAtUsers = previousKeyResultDetail.getAtUsers();
            boolean res = this.updateById(keyResultDetail);
            if (keyResultDetail.getUsers() != null && keyResultDetail.getUsers().size() > 0 && periodName != null) {
                for (FeishuUser user : keyResultDetail.getUsers()) {
                    if (!previousAtUsers.contains(user.getEmployeeNo()))
                        feishuService.sendPersonalMessage(user, feishuService.getMarkdownMessage(getMarkDownMessage(keyResultDetail, periodName), null));
                }
            }
            return res;
        } else {
            if (keyResultDetail.getUsers() != null && keyResultDetail.getUsers().size() > 0 && periodName != null) {
                for (FeishuUser user : keyResultDetail.getUsers()) {
                    feishuService.sendPersonalMessage(user, feishuService.getMarkdownMessage(getMarkDownMessage(keyResultDetail, periodName), null));
                }
            }
            return this.add(keyResultDetail);
        }

    }

    @Override
    public String getMarkDownMessage(KeyResultDetail keyResultDetail, String periodName) {
        MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
        markdownGroupMessage.setTitle("有一条Key Result提及到您：");
        markdownGroupMessage.addBlobContent("周期：" + periodName);
        markdownGroupMessage.addContent("Key Result内容：" + keyResultDetail.getKeyResultName());
        String atUsers = keyResultDetail.getUsers().stream().map(FeishuUser::getName).collect(Collectors.joining(","));
        markdownGroupMessage.addContent("提及人员：" + atUsers);
        markdownGroupMessage.addContent("[查看详情](https://open.feishu.cn/open-apis/authen/v1/index?app_id=cli_a3f634b596a3100c&redirect_uri=http://udsapi.aacoptics.com/okrFill)");
        return markdownGroupMessage.toString();
    }
}