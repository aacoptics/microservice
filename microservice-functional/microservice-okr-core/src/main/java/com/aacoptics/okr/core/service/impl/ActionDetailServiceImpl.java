package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.ActionDetail;
import com.aacoptics.okr.core.entity.po.FeishuUser;
import com.aacoptics.okr.core.entity.vo.MarkdownGroupMessage;
import com.aacoptics.okr.core.mapper.ActionDetailMapper;
import com.aacoptics.okr.core.service.ActionDetailService;
import com.aacoptics.okr.core.service.FeishuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActionDetailServiceImpl extends ServiceImpl<ActionDetailMapper, ActionDetail> implements ActionDetailService {

    @Resource
    FeishuService feishuService;

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
        if (actionDetail.getId() != null) {
            ActionDetail previousActionDetail = getById(actionDetail.getId());
            String previousAtUsers = previousActionDetail.getAtUsers();
            boolean res = this.updateById(actionDetail);
            if (actionDetail.getUsers() != null && actionDetail.getUsers().size() > 0) {
                for (FeishuUser user : actionDetail.getUsers()) {
                    if (previousAtUsers == null || !previousAtUsers.contains(user.getEmployeeNo()))
                        feishuService.sendPersonalMessage(user, feishuService.getMarkdownMessage(getMarkDownMessage(actionDetail), null));
                }
            }
            return res;
        } else {
            if (actionDetail.getUsers() != null && actionDetail.getUsers().size() > 0) {
                for (FeishuUser user : actionDetail.getUsers()) {
                    feishuService.sendPersonalMessage(user, feishuService.getMarkdownMessage(getMarkDownMessage(actionDetail), null));
                }
            }
            return this.add(actionDetail);
        }
    }

    @Override
    public String getMarkDownMessage(ActionDetail actionDetail) {
        MarkdownGroupMessage markdownGroupMessage = new MarkdownGroupMessage();
        markdownGroupMessage.setTitle("有一条行动项提及到您：");
        markdownGroupMessage.addContent("行动项：" + actionDetail.getActionName());
        markdownGroupMessage.addContent("所需支持：" + actionDetail.getSupportDetail());
        markdownGroupMessage.addContent("截止日期：" + actionDetail.getDueTime().toLocalDate().toString());
        markdownGroupMessage.addContent("行动项：" + actionDetail.getActionName());
        String atUsers = actionDetail.getUsers().stream().map(FeishuUser::getName).collect(Collectors.joining(","));
        markdownGroupMessage.addContent("提及人员：" + atUsers);
        markdownGroupMessage.addContent("[查看详情](https://open.feishu.cn/open-apis/authen/v1/index?app_id=cli_a3f634b596a3100c&redirect_uri=http://udsapi.aacoptics.com/okrFill)");
        return markdownGroupMessage.toString();
    }
}
