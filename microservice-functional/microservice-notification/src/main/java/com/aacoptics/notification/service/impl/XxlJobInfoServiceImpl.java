package com.aacoptics.notification.service.impl;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.notification.entity.form.TriggerJobForm;
import com.aacoptics.notification.entity.po.XxlJobInfo;
import com.aacoptics.notification.entity.po.XxlJobResult;
import com.aacoptics.notification.mapper.XxlJobInfoMapper;
import com.aacoptics.notification.provider.XxlJobProvider;
import com.aacoptics.notification.service.XxlJobInfoService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import feign.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Service
@Slf4j
@DS("msg_db")
public class XxlJobInfoServiceImpl extends ServiceImpl<XxlJobInfoMapper, XxlJobInfo> implements XxlJobInfoService {
    @Resource
    XxlJobInfoMapper xxlJobInfoMapper;

    @Resource
    XxlJobProvider xxlJobProvider;

    @Override
    public Result add(XxlJobInfo xxlJobInfo) {
        xxlJobInfo.setId(xxlJobInfoMapper.maxId() + 1);
        xxlJobInfo.setAddTime(new Date());
        xxlJobInfo.setUpdateTime(new Date());
        xxlJobInfo.setGlueUpdatetime(new Date());
        //return this.save(xxlJobInfo);
        XxlJobResult xxlJobResult = xxlJobProvider.add(xxlJobInfo);
        if (xxlJobResult.getCode() == 200)
            return Result.success("添加成功");
        else
            return Result.fail(xxlJobResult);
    }

    @Override
    public Result update(XxlJobInfo xxlJobInfo) {
        xxlJobInfo.setUpdateTime(new Date());
        XxlJobResult xxlJobResult = xxlJobProvider.update(xxlJobInfo);
        if (xxlJobResult.getCode() == 200)
            return Result.success("更新成功");
        else
            return Result.fail(xxlJobResult);
        //return this.updateById(xxlJobInfo);
    }

    @Override
    public Result delete(Integer id) {
        //return this.removeById(id);
        XxlJobResult xxlJobResult = xxlJobProvider.remove(id);
        if (xxlJobResult.getCode() == 200)
            return Result.success("删除成功");
        else
            return Result.fail(xxlJobResult);
    }

    @Override
    public Result start(Integer id) {
        //return this.removeById(id);
        XxlJobResult xxlJobResult = xxlJobProvider.start(id);
        if (xxlJobResult.getCode() == 200)
            return Result.success("启动成功");
        else
            return Result.fail(xxlJobResult);
    }

    @Override
    public Result stop(Integer id) {
        //return this.removeById(id);
        XxlJobResult xxlJobResult = xxlJobProvider.stop(id);
        if (xxlJobResult.getCode() == 200)
            return Result.success("停止成功");
        else
            return Result.fail(xxlJobResult);
    }

    @Override
    public IPage<XxlJobInfo> query(Page page, XxlJobInfo xxlJobInfo) {
        QueryWrapper<XxlJobInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(xxlJobInfo.getJobDesc()), "job_desc", xxlJobInfo.getJobDesc());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<XxlJobInfo> listNotificationTask(Page page, XxlJobInfo xxlJobInfo, Integer searchOption, String username) {
        if (searchOption == 0)
            return xxlJobInfoMapper.listNotificationTask(page, xxlJobInfo.getPlanKey(), username);
        else if (searchOption == 1)
            return xxlJobInfoMapper.listSubNotificationTask(page, xxlJobInfo.getPlanKey(), username);
        else if (searchOption == 2)
            return xxlJobInfoMapper.listSubNotificationTaskByUser(page, xxlJobInfo.getPlanKey(), username);
        else
            return new Page<>();
    }

    @Override
    public Result triggerJob(TriggerJobForm triggerJobForm) {
        XxlJobResult xxlJobResult = xxlJobProvider.triggerJob(triggerJobForm.getJobId(),
                triggerJobForm.getExecutorParam(),
                triggerJobForm.getAddressList());
        if (xxlJobResult.getCode() == 200)
            return Result.success("触发成功");
        else
            return Result.fail(xxlJobResult);
    }
}