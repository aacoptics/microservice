package com.aacoptics.gaia.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.gaia.entity.form.PlanActualPerPersonForm;
import com.aacoptics.gaia.entity.po.FeishuMessage;
import com.aacoptics.gaia.entity.po.PlanActualPerPerson;
import com.aacoptics.gaia.entity.vo.MessageInfo;
import com.aacoptics.gaia.mapper.PlanActualPerPersonMapper;
import com.aacoptics.gaia.provider.NotificationProvider;
import com.aacoptics.gaia.service.IPlanActualPerPersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PlanActualPerPersonService extends ServiceImpl<PlanActualPerPersonMapper, PlanActualPerPerson> implements IPlanActualPerPersonService {

    @Resource
    PlanActualPerPersonMapper planActualPerPersonMapper;

    @Resource
    NotificationProvider notificationProvider;

    @Override
    public List<PlanActualPerPerson> getPlanInfoByTime(LocalDateTime startDate, LocalDateTime endDate) {
        return planActualPerPersonMapper.getPlanInfoByTime(startDate, endDate);
    }

    @Override
    public void updatePlanResult(List<PlanActualPerPersonForm> planActualPerPersons){
        for (PlanActualPerPersonForm planActualPerPerson : planActualPerPersons) {
            UpdateWrapper<PlanActualPerPerson> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("ID", planActualPerPerson.getId());
            updateWrapper.set("gy_transfer_flg",  planActualPerPerson.getGyTransferFlg());
            updateWrapper.set("transfer_err",  planActualPerPerson.getTransferErr());
            baseMapper.update(null, updateWrapper);
        }
    }

    @Override
    public List<MessageInfo> getPersonPlanMsg(){
//        LocalDate previousDate = LocalDate.now().minusDays(1);
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String previousDateStr = previousDate.format(df);
        List<MessageInfo> messages = new ArrayList<>();
        List<PlanActualPerPerson> workDateList = getWorkDate();
        if(workDateList.size() <= 0)
            return messages;
        for (PlanActualPerPerson workDate : workDateList) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
            LocalDate workDateLocal = LocalDate.parse(workDate.getWorkDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            String workDateStr = workDateLocal.format(df);
            List<PlanActualPerPerson> needToSendMsg = getDataByWorkDate(workDate.getWorkDate());
            if(needToSendMsg.size() > 0){
                String jobNumber = "";
                StringBuilder message = new StringBuilder();
                List<Integer> idList = new ArrayList<>();
                for (PlanActualPerPerson planActualPerPerson : needToSendMsg) {
                    if(!planActualPerPerson.getEmployeeID().equals(jobNumber)){
                        if(!StrUtil.isBlank(message.toString())){
                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.setJobNumber(jobNumber);
                            messageInfo.setMessage(message.toString().trim());
                            messageInfo.setIdList(idList);
                            messages.add(messageInfo);
                            message = new StringBuilder();
                            idList = new ArrayList<>();
                        }
                        jobNumber = planActualPerPerson.getEmployeeID();
                    }
                    if(StrUtil.isBlank(message.toString())){
                        message.append(planActualPerPerson.getName())
                                .append("，您好\n")
                                .append(workDateStr).append("\n")
                                .append("出勤时长：").append(planActualPerPerson.getWorkTime()).append("\n")
                                .append("\n")
                                .append("生产拆分工时：").append(planActualPerPerson.getSplitWorkTime()).append("\n")
                                .append("计划生产数量：").append(planActualPerPerson.getPlanDailyCapacityOfProduction().intValue()).append("\n")
                                .append("实际生产数量：").append(planActualPerPerson.getActualDailyCapacityOfProduction().intValue()).append("\n")
                                .append("质量扣除数量：").append(planActualPerPerson.getCapacityOfProductionDeduct().intValue()).append("\n")
                                .append("综合产能：").append(planActualPerPerson.getComprehensiveCapacityOfProduction().intValue()).append("\n")
                                .append("\n");
                    }
                    else{
                        message.append("生产拆分工时：").append(planActualPerPerson.getSplitWorkTime()).append("\n")
                                .append("计划生产数量：").append(planActualPerPerson.getPlanDailyCapacityOfProduction().intValue()).append("\n")
                                .append("实际生产数量：").append(planActualPerPerson.getActualDailyCapacityOfProduction().intValue()).append("\n")
                                .append("质量扣除数量：").append(planActualPerPerson.getCapacityOfProductionDeduct().intValue()).append("\n")
                                .append("综合产能：").append(planActualPerPerson.getComprehensiveCapacityOfProduction().intValue()).append("\n")
                                .append("\n");
                    }
                    idList.add(planActualPerPerson.getId());
                }
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setJobNumber(jobNumber);
                messageInfo.setIdList(idList);
                messageInfo.setMessage(message.toString().trim());
                messages.add(messageInfo);
            }
        }
        return messages;
    }

    @Override
    public void sendFeishuMessage(){
        List<MessageInfo> messageInfos = getPersonPlanMsg();
        if(messageInfos.size() > 0){
            for (MessageInfo messageInfo : messageInfos) {
                FeishuMessage feishuMessage = new FeishuMessage();
                feishuMessage.setJobNumber(messageInfo.getJobNumber());
                feishuMessage.setContent(messageInfo.getMessage());
                Result res = notificationProvider.sendFeishuNotification(feishuMessage);
                if(res.isSuccess()){
                    UpdateWrapper<PlanActualPerPerson> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("dd_transfer_flg", 1)
                            .in("ID", messageInfo.getIdList());
                    this.update(null, updateWrapper);
                }
                else{
                    UpdateWrapper<PlanActualPerPerson> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("dd_transfer_flg", 2)
                            .in("ID", messageInfo.getIdList());
                    this.update(null, updateWrapper);
                }
            }
        }
    }

    @Override
    public List<PlanActualPerPerson> getDataByWorkDate(String workDate){
        QueryWrapper<PlanActualPerPerson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_date", workDate)
                .eq("dd_transfer_flg", 0)
                .orderByAsc("EmployeeID");
        return this.list(queryWrapper);
    }

    @Override
    public List<PlanActualPerPerson> getWorkDate(){
        QueryWrapper<PlanActualPerPerson> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("work_date")
                .eq("dd_transfer_flg", 0)
                .groupBy("work_date");
        return this.list(queryWrapper);
    }
}